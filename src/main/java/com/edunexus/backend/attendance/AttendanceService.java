package com.edunexus.backend.attendance;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.edunexus.backend.student.Student;
import com.edunexus.backend.student.StudentRepository;
import com.edunexus.backend.teacher.Teacher;
import com.edunexus.backend.teacher.TeacherRepository;

@Service
public class AttendanceService {

    @Autowired private AttendanceRepository attendanceRepo;
    @Autowired private StudentRepository studentRepo;

    @Autowired(required = false)
    private TeacherRepository teacherRepo;

    private void validateTeacherForClass(String teacherId, int classId) {
        if (teacherRepo == null) return; // if you don't have teacherRepo wired

        Teacher t = teacherRepo.findById(teacherId)
            .orElseThrow(() -> new RuntimeException("TEACHER_NOT_FOUND"));

        int teacherClass = Integer.parseInt(String.valueOf(t.getTeacher_class()));
        if (teacherClass != classId) {
            throw new RuntimeException("FORBIDDEN_TEACHER_NOT_ASSIGNED_TO_CLASS");
        }
    }

    private void validateNumbers(int total, int present, int absent, int late) {
        if (total < 0 || present < 0 || absent < 0 || late < 0) {
            throw new RuntimeException("NEGATIVE_VALUES_NOT_ALLOWED");
        }
        if (present + absent + late > total) {
            throw new RuntimeException("P_A_L_EXCEEDS_TOTAL");
        }
    }

    private int parseIntCell(String[] c, int idx, String errKey) {
        if (c.length <= idx) throw new RuntimeException(errKey);
        String v = c[idx] == null ? "" : c[idx].trim();
        if (v.isBlank()) throw new RuntimeException(errKey);
        try { return Integer.parseInt(v); }
        catch (Exception e) { throw new RuntimeException(errKey); }
    }

    private void validateYearMonth(String yearMonth) {
        // expected "YYYY-MM"
        if (yearMonth == null || !yearMonth.matches("\\d{4}-\\d{2}")) {
            throw new RuntimeException("INVALID_YEAR_MONTH");
        }
    }

    @Transactional
    public AttendanceUploadResult uploadCsv(String teacherId, int classId, String yearMonth, MultipartFile file) throws Exception {
        validateTeacherForClass(teacherId, classId);
        validateYearMonth(yearMonth);

        List<String[]> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            boolean first = true;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isBlank()) continue;

                // header skip
                if (first && line.toLowerCase().contains("studentid")) {
                    first = false;
                    continue;
                }
                first = false;

                rows.add(line.split(",", -1));
            }
        }

        AttendanceUploadResult result = new AttendanceUploadResult(rows.size());

        for (String[] c : rows) {
            String studentId = c.length > 0 ? c[0].trim() : "";

            if (studentId.isBlank()) {
                result.incFailed();
                result.addRow(new AttendanceUploadRowResult("", "FAILED", "-", "EMPTY_STUDENT_ID"));
                continue;
            }

            try {
                Student s = studentRepo.findById(studentId)
                        .orElseThrow(() -> new RuntimeException("STUDENT_NOT_FOUND"));

                if (s.getStud_class() != classId) {
                    throw new RuntimeException("STUDENT_NOT_IN_CLASS");
                }

                int totalDays = parseIntCell(c, 1, "INVALID_TOTAL_DAYS");
                int present   = parseIntCell(c, 2, "INVALID_PRESENT");
                int absent    = parseIntCell(c, 3, "INVALID_ABSENT");
                int late      = (c.length > 4 && c[4] != null && !c[4].trim().isBlank())
                        ? Integer.parseInt(c[4].trim())
                        : 0;

                validateNumbers(totalDays, present, absent, late);

                Optional<AttendanceMonth> existing =
                        attendanceRepo.findByStudentAndMonth(studentId, yearMonth);

                AttendanceMonth a = existing.orElseGet(AttendanceMonth::new);
                a.setStudentId(studentId);
                a.setClassId(classId);
                a.setYearMonth(yearMonth);

                // ✅ UPSERT (replace month values)
                a.setTotalDays(totalDays);
                a.setPresent(present);
                a.setAbsent(absent);
                a.setLate(late);

                a.setUpdatedBy(teacherId);
                a.setUpdatedAt(LocalDateTime.now());

                attendanceRepo.save(a);

                result.incSuccess();
                result.addRow(new AttendanceUploadRowResult(
                        studentId, "OK", existing.isPresent() ? "UPDATED" : "INSERTED", ""
                ));
            } catch (Exception ex) {
                // ✅ "will not accept that student's attendance"
                // => skip that row, do not stop the import
                result.incFailed();
                result.addRow(new AttendanceUploadRowResult(studentId, "FAILED", "-", ex.getMessage()));
            }
        }

        return result;
    }

    public List<AttendanceMonth> getForTeacher(int classId, String yearMonth, String teacherId) {
        validateTeacherForClass(teacherId, classId);
        validateYearMonth(yearMonth);
        return attendanceRepo.findByClassAndMonth(classId, yearMonth);
    }

    public record AttendanceMonthStatusDTO(boolean uploaded, int count) {}

    public AttendanceMonthStatusDTO monthStatus(String teacherId, int classId, String yearMonth) {
        validateTeacherForClass(teacherId, classId);
        validateYearMonth(yearMonth);

        int count = attendanceRepo.countByClassAndMonth(classId, yearMonth);
        return new AttendanceMonthStatusDTO(count > 0, count);
    }

    public AttendanceSummaryDTO getMySummary(String studentId) {
        List<Object[]> rows = attendanceRepo.totalsForStudent(studentId);

        Object[] t = (rows == null || rows.isEmpty() || rows.get(0) == null)
                ? new Object[] {0, 0, 0, 0}
                : rows.get(0);

        int total = ((Number) t[0]).intValue();
        int present = ((Number) t[1]).intValue();
        int absent = ((Number) t[2]).intValue();
        int late = ((Number) t[3]).intValue();

        return new AttendanceSummaryDTO(total, present, absent, late);
    }

    public List<AttendanceStudentMonthDTO> getMyMonthlyDTO(String studentId) {
        List<AttendanceMonth> rows = attendanceRepo.findAllForStudent(studentId);

        List<AttendanceStudentMonthDTO> out = new java.util.ArrayList<>();
        for (AttendanceMonth a : rows) {
            int total = a.getTotalDays();
            int present = a.getPresent();
            int absent = a.getAbsent();
            int late = a.getLate();
            double pct = total <= 0 ? 0.0 : (present * 100.0 / total);

            out.add(new AttendanceStudentMonthDTO(
                    a.getYearMonth(),
                    total,
                    present,
                    absent,
                    late,
                    pct
            ));
        }
        return out;
    }


    public List<AttendanceMonth> getMyMonthly(String studentId) {
        return attendanceRepo.findAllForStudent(studentId);
    }

    
    @Transactional
    public AttendanceMonth updateSingle(String teacherId, int classId, String studentId, String yearMonth, AttendanceUpdateRequest req) {
        validateTeacherForClass(teacherId, classId);
        validateYearMonth(yearMonth);

        Student s = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("STUDENT_NOT_FOUND"));

        if (s.getStud_class() != classId) {
            throw new RuntimeException("STUDENT_NOT_IN_CLASS");
        }

        // ✅ do NOT create if missing
        AttendanceMonth a = attendanceRepo.findByStudentAndMonth(studentId, yearMonth)
                .orElseThrow(() -> new RuntimeException("ATTENDANCE_NOT_UPLOADED_FOR_MONTH"));

        int total = req.getTotalDays() == null ? a.getTotalDays() : req.getTotalDays();
        int present = req.getPresent() == null ? a.getPresent() : req.getPresent();
        int absent = req.getAbsent() == null ? a.getAbsent() : req.getAbsent();
        int late = req.getLate() == null ? a.getLate() : req.getLate();

        validateNumbers(total, present, absent, late);

        a.setTotalDays(total);
        a.setPresent(present);
        a.setAbsent(absent);
        a.setLate(late);

        a.setUpdatedBy(teacherId);
        a.setUpdatedAt(LocalDateTime.now());

        return attendanceRepo.save(a);
    }


    public List<AttendanceTeacherRowDTO> getStudentsOfClassWithMonth(String teacherId, int classId, String yearMonth) {
        validateTeacherForClass(teacherId, classId);
        validateYearMonth(yearMonth);

        // 1) all students in this class
        List<Student> students = studentRepo.findByStudClass(classId);

        // 2) all attendance rows that exist for this class+month
        List<AttendanceMonth> monthRows = attendanceRepo.findByClassAndMonth(classId, yearMonth);

        // 3) map by studentId for fast merge
        java.util.Map<String, AttendanceMonth> map = new java.util.HashMap<>();
        for (AttendanceMonth a : monthRows) map.put(a.getStudentId(), a);

        // 4) build output: always return all students (even if attendance not uploaded)
        List<AttendanceTeacherRowDTO> out = new java.util.ArrayList<>();

        for (Student s : students) {
            AttendanceMonth a = map.get(s.getStud_id());

            if (a == null) {
                // ✅ month not uploaded for this student => return null values
                out.add(new AttendanceTeacherRowDTO(
                        s.getStud_id(),
                        s.getStud_name(),
                        s.getStud_email(),
                        s.getStud_address(),
                        s.getStud_guardian(),
                        String.valueOf(s.getStud_mobile()),
                        classId,
                        yearMonth,
                        null, null, null, null,
                        false
                ));
            } else {
                // ✅ uploaded row exists
                out.add(new AttendanceTeacherRowDTO(
                        s.getStud_id(),
                        s.getStud_name(),
                        s.getStud_email(),
                        s.getStud_address(),
                        s.getStud_guardian(),
                        String.valueOf(s.getStud_mobile()),
                        classId,
                        yearMonth,
                        a.getTotalDays(),
                        a.getPresent(),
                        a.getAbsent(),
                        a.getLate(),
                        true
                ));
            }
        }

        return out;
    }


}
