package com.edunexus.backend.attendance;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Autowired private AttendanceSessionRepository sessionRepo;
    @Autowired private AttendanceAbsenceRepository absenceRepo;

    @Autowired private StudentRepository studentRepo;
    @Autowired private TeacherRepository teacherRepo;

    // ✅ Teacher must be allowed for this class
    private void validateTeacherForClass(String teacherId, int classId) {
        Teacher t = teacherRepo.findById(teacherId)
            .orElseThrow(() -> new RuntimeException("Teacher not found: " + teacherId));

        // your teacher_class field might be String like "10" or "10,11"
        // Since you said now teacher handles only 1 class, keep it simple:
        int teacherClass = Integer.parseInt(String.valueOf(t.getTeacher_class()));
        if (teacherClass != classId) {
            throw new RuntimeException("Forbidden: teacher not assigned to class " + classId);
        }
    }

    public Map<String, Object> getAttendanceForClassByDate(int classId, LocalDate date) {

        // session indicates "attendance was marked"
        Optional<AttendanceSession> sessionOpt = sessionRepo.findByClassIdAndDate(classId, date);

        if (sessionOpt.isEmpty()) {
            return Map.of(
                "marked", false,
                "date", date.toString(),
                "classId", classId,
                "rows", List.of()
            );
        }

        AttendanceSession session = sessionOpt.get();

        // All students of that class
     // All students of that class
        List<Student> students = studentRepo.findByStudClass(classId);

        // absentees/late for that session
        List<AttendanceAbsence> absentList = absenceRepo.findBySessionId(session.getId());

        // Map: studentId -> status (ABSENT/LATE)
        Map<String, AttendanceStatus> absentMap = absentList.stream()
            .collect(Collectors.toMap(
                AttendanceAbsence::getStudentId,
                AttendanceAbsence::getStatus,
                (oldVal, newVal) -> newVal // handle duplicates safely
            ));

        List<AttendanceRowDTO> rows = students.stream().map(s -> {
            AttendanceStatus status = absentMap.get(s.getStud_id());
            String st = (status == null) ? "PRESENT" : status.name();

            return new AttendanceRowDTO(
                s.getStud_id(),
                s.getStud_name(),
                s.getStud_email(),
                s.getStud_address(),
                s.getStud_guardian(),
                st
            );
        }).toList();


        return Map.of(
            "marked", true,
            "date", date.toString(),
            "classId", classId,
            "rows", rows
        );
    }
    
    @Transactional
    public AttendanceSession upsertSession(int classId, LocalDate date, String teacherId) {
        return sessionRepo.findByClassIdAndDate(classId, date).orElseGet(() -> {
            AttendanceSession s = new AttendanceSession();
            s.setClassId(classId);
            s.setDate(date);
            s.setTeacherId(teacherId);
            return sessionRepo.save(s);
        });
    }

    private boolean studentBelongsToClass(String studentId, int classId) {
        Optional<Student> s = studentRepo.findById(studentId);
        return s.isPresent() && s.get().getStud_class() == classId;
    }

    private AttendanceStatus parseStatus(String raw) {
        if (raw == null || raw.isBlank()) return AttendanceStatus.ABSENT;
        raw = raw.trim().toUpperCase();
        if (raw.equals("LATE")) return AttendanceStatus.LATE;
        return AttendanceStatus.ABSENT;
    }

    /** Replace attendance absences for day: delete old rows, insert new valid ones */
    @Transactional
    public AttendanceUploadResult uploadAbsenteesCsv(
            String teacherId,
            int classId,
            LocalDate date,
            MultipartFile file
    ) throws Exception {

        validateTeacherForClass(teacherId, classId);

        // session
        AttendanceSession session = upsertSession(classId, date, teacherId);

        // Replace day data
        absenceRepo.deleteBySessionId(session.getId());

        // Read CSV
        List<String[]> parsed = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            boolean first = true;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isBlank()) continue;

                // skip header if present
                if (first && line.toLowerCase().contains("studentid")) {
                    first = false;
                    continue;
                }
                first = false;

                String[] cols = line.split(",", -1);
                parsed.add(cols);
            }
        }

        AttendanceUploadResult result = new AttendanceUploadResult(parsed.size());

        // insert rows
        for (String[] cols : parsed) {
            String studentId = (cols.length >= 1) ? cols[0].trim() : "";
            String statusRaw = (cols.length >= 2) ? cols[1].trim() : "";

            if (studentId.isBlank()) {
                result.incFailed();
                result.addRow(new AttendanceUploadRowResult("", "", "FAILED", "EMPTY_STUDENT_ID"));
                continue;
            }

            // ✅ validate student exists AND belongs to class
            if (!studentBelongsToClass(studentId, classId)) {
                result.incFailed();
                result.addRow(new AttendanceUploadRowResult(studentId, statusRaw, "FAILED",
                        "STUDENT_NOT_FOUND_OR_NOT_IN_CLASS"));
                continue;
            }

            AttendanceStatus status = parseStatus(statusRaw);

            AttendanceAbsence a = new AttendanceAbsence();
            a.setSessionId(session.getId());
            a.setStudentId(studentId);
            a.setStatus(status);
            absenceRepo.save(a);

            result.incSuccess();
            result.addRow(new AttendanceUploadRowResult(studentId, status.name(), "OK", ""));
        }

        return result;
    }

    /** Manual add/replace for day */
    @Transactional
    public AttendanceUploadResult uploadManual(
            String teacherId,
            AttendanceManualRequest req
    ) {
        int classId = req.getClassId();
        LocalDate date = LocalDate.parse(req.getDate());

        validateTeacherForClass(teacherId, classId);

        AttendanceSession session = upsertSession(classId, date, teacherId);

        // replace whole day
        absenceRepo.deleteBySessionId(session.getId());

        List<AttendanceManualRequest.Entry> entries = req.getEntries() == null ? List.of() : req.getEntries();
        AttendanceUploadResult result = new AttendanceUploadResult(entries.size());

        for (AttendanceManualRequest.Entry e : entries) {
            String studentId = e.getStudentId() == null ? "" : e.getStudentId().trim();
            String statusRaw = e.getStatus() == null ? "" : e.getStatus().trim();

            if (studentId.isBlank()) {
                result.incFailed();
                result.addRow(new AttendanceUploadRowResult("", "", "FAILED", "EMPTY_STUDENT_ID"));
                continue;
            }

            if (!studentBelongsToClass(studentId, classId)) {
                result.incFailed();
                result.addRow(new AttendanceUploadRowResult(studentId, statusRaw, "FAILED",
                        "STUDENT_NOT_FOUND_OR_NOT_IN_CLASS"));
                continue;
            }

            AttendanceStatus status = parseStatus(statusRaw);

            AttendanceAbsence a = new AttendanceAbsence();
            a.setSessionId(session.getId());
            a.setStudentId(studentId);
            a.setStatus(status);
            absenceRepo.save(a);

            result.incSuccess();
            result.addRow(new AttendanceUploadRowResult(studentId, status.name(), "OK", ""));
        }

        return result;
    }
}
