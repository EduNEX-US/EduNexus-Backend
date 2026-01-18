package com.edunexus.backend.marks;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.edunexus.backend.student.Student;
import com.edunexus.backend.student.StudentRepository;
import com.edunexus.backend.teacher.Teacher;
import com.edunexus.backend.teacher.TeacherRepository;

@Service
public class MarksService {

    @Autowired private MarksRepository marksRepo;
    @Autowired private StudentRepository studentRepo;

    @Autowired(required = false) private TeacherRepository teacherRepo;

    private ExamSession parseSession(String s) {
        if (s == null || s.trim().isEmpty()) {
            throw new RuntimeException("EXAM_SESSION_REQUIRED");
        }
        try {
            return ExamSession.valueOf(s.trim().toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("INVALID_EXAM_SESSION");
        }
    }

    private void validateTeacherForClass(String teacherId, int classId) {
        if (teacherRepo == null) return;

        Teacher t = teacherRepo.findById(teacherId)
            .orElseThrow(() -> new RuntimeException("Teacher not found: " + teacherId));

        int teacherClass = Integer.parseInt(String.valueOf(t.getTeacher_class()));
        if (teacherClass != classId) {
            throw new RuntimeException("Forbidden: teacher not assigned to class " + classId);
        }
    }

    private Student requireStudentInClass(String studentId, int classId) {
        Student s = studentRepo.findById(studentId)
            .orElseThrow(() -> new RuntimeException("STUDENT_NOT_FOUND"));

        if (s.getStud_class() != classId) {
            throw new RuntimeException("STUDENT_NOT_IN_CLASS");
        }
        return s;
    }

    private void applySubjectRule(int classId, Marks m, Double gk, Double ss) {
        // ✅ Requirement:
        // Class <=5 => GK used, SS null
        // Class >=6 => SS used, GK null
        if (classId <= 5) {
            m.setGeneralKnowledge(gk != null ? gk : ss); // fallback if frontend sends SS by mistake
            m.setSocialScience(null);
        } else {
            m.setSocialScience(ss != null ? ss : gk);    // fallback if frontend sends GK by mistake
            m.setGeneralKnowledge(null);
        }
    }

    private void applyCommon(Marks m,
                             Double english, Double hindi, Double math,
                             Double science, Double computer,
                             LocalDate resultDate,
                             int classId,
                             ExamSession examSession) {
        m.setEnglish(english);
        m.setHindi(hindi);
        m.setMath(math);
        m.setScience(science);
        m.setComputer(computer);
        m.setResultDate(resultDate);

        m.setClassId(classId);
        m.setExamSession(examSession);
    }

    // ✅ Manual upsert by (studentId + examSession)
    @Transactional
    public MarksUploadResult uploadManual(String teacherId, MarksManualRequest req) {
        int classId = req.getClassId();
        LocalDate date = LocalDate.parse(req.getResultDate());
        ExamSession session = parseSession(req.getExamSession());

        validateTeacherForClass(teacherId, classId);

        List<MarksManualRequest.Entry> entries =
            req.getEntries() == null ? List.of() : req.getEntries();

        MarksUploadResult result = new MarksUploadResult(entries.size());

        for (MarksManualRequest.Entry e : entries) {
            String studentId = e.getStudentId() == null ? "" : e.getStudentId().trim();
            if (studentId.isBlank()) {
                result.incFailed();
                result.addRow(new MarksUploadRowResult("", "FAILED", "-", "EMPTY_STUDENT_ID"));
                continue;
            }

            try {
                Student student = requireStudentInClass(studentId, classId);

                Optional<Marks> existing =
                    marksRepo.findOneByStudentIdAndSession(studentId, session);

                Marks m = existing.orElseGet(Marks::new);
                m.setStudent(student);

                applyCommon(m,
                    e.getEnglish(), e.getHindi(), e.getMath(),
                    e.getScience(), e.getComputer(),
                    date, classId, session
                );

                applySubjectRule(classId, m, e.getGk(), e.getSocialScience());

                marksRepo.save(m);

                result.incSuccess();
                result.addRow(new MarksUploadRowResult(
                    studentId, "OK", existing.isPresent() ? "UPDATED" : "INSERTED", ""
                ));
            } catch (Exception ex) {
                result.incFailed();
                result.addRow(new MarksUploadRowResult(studentId, "FAILED", "-", ex.getMessage()));
            }
        }

        return result;
    }

    // ✅ CSV upsert by (studentId + examSession)
    // Supported CSV columns:
    // studentId, english, hindi, math, science, gk, socialScience, computer
    @Transactional
    public MarksUploadResult uploadCsv(String teacherId, int classId, String examSession, String resultDate, MultipartFile file)
            throws Exception {

        LocalDate date = LocalDate.parse(resultDate);
        ExamSession session = parseSession(examSession);

        validateTeacherForClass(teacherId, classId);

        List<String[]> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            boolean first = true;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isBlank()) continue;

                if (first && line.toLowerCase().contains("studentid")) {
                    first = false;
                    continue;
                }
                first = false;

                rows.add(line.split(",", -1));
            }
        }

        MarksUploadResult result = new MarksUploadResult(rows.size());

        for (String[] c : rows) {
            String studentId = c.length > 0 ? c[0].trim() : "";
            if (studentId.isBlank()) {
                result.incFailed();
                result.addRow(new MarksUploadRowResult("", "FAILED", "-", "EMPTY_STUDENT_ID"));
                continue;
            }

            try {
                Student student = requireStudentInClass(studentId, classId);

                Double english = parseDouble(c, 1);
                Double hindi   = parseDouble(c, 2);
                Double math    = parseDouble(c, 3);
                Double science = parseDouble(c, 4);
                Double gk      = parseDouble(c, 5);
                Double ss      = parseDouble(c, 6);
                Double computer= parseDouble(c, 7);

                Optional<Marks> existing =
                    marksRepo.findOneByStudentIdAndSession(studentId, session);

                Marks m = existing.orElseGet(Marks::new);
                m.setStudent(student);

                applyCommon(m, english, hindi, math, science, computer, date, classId, session);
                applySubjectRule(classId, m, gk, ss);

                marksRepo.save(m);

                result.incSuccess();
                result.addRow(new MarksUploadRowResult(
                    studentId, "OK", existing.isPresent() ? "UPDATED" : "INSERTED", ""
                ));
            } catch (Exception ex) {
                result.incFailed();
                result.addRow(new MarksUploadRowResult(studentId, "FAILED", "-", ex.getMessage()));
            }
        }

        return result;
    }

    @Transactional
    public void deleteMarksById(String teacherId, Long id) {
        Marks m = marksRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MARKS_NOT_FOUND"));

        // ✅ Optional security check: teacher must match the class
        validateTeacherForClass(teacherId, m.getClassId());

        marksRepo.delete(m);
    }

    
    // ✅ Teacher fetch: all students marks by class + session
    public List<Marks> getMarksForClassSession(String teacherId, int classId, String examSession) {
        validateTeacherForClass(teacherId, classId);
        ExamSession session = parseSession(examSession);
        return marksRepo.findAllByClassIdAndSession(classId, session);
    }

    // ✅ Student fetch: my marks by session
    public Marks getMyMarksForSession(String studentId, String examSession) {
        ExamSession session = parseSession(examSession);
        return marksRepo.findMyMarksForSession(studentId, session)
            .orElseThrow(() -> new RuntimeException("MARKS_NOT_FOUND"));
    }

    private Double parseDouble(String[] c, int idx) {
        if (c.length <= idx) return null;
        String v = c[idx] == null ? "" : c[idx].trim();
        if (v.isBlank()) return null;
        try { return Double.parseDouble(v); }
        catch (Exception e) { throw new RuntimeException("INVALID_NUMBER_AT_COL_" + (idx + 1)); }
    }
}
