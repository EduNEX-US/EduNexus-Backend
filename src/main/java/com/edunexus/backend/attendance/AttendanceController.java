package com.edunexus.backend.attendance;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/attendance")
@CrossOrigin("*")
public class AttendanceController {

    @Autowired private AttendanceService attendanceService;

    // ✅ Teacher uploads monthly CSV for his class
    @PostMapping(value = "/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadAttendanceCsv(
            Authentication auth,
            @RequestParam("classId") int classId,
            @RequestParam("yearMonth") String yearMonth,
            @RequestParam("file") MultipartFile file
    ) throws Exception {
        String teacherId = (String) auth.getPrincipal();
        return ResponseEntity.ok(attendanceService.uploadCsv(teacherId, classId, yearMonth, file));
    }

    // ✅ Teacher view monthly list
    @GetMapping("/teacher")
    public ResponseEntity<?> getForTeacher(
            Authentication auth,
            @RequestParam("classId") int classId,
            @RequestParam("yearMonth") String yearMonth
    ) {
        String teacherId = (String) auth.getPrincipal();
        List<AttendanceMonth> rows = attendanceService.getForTeacher(classId, yearMonth, teacherId);
        return ResponseEntity.ok(rows);
    }
    
    @GetMapping("/teacher/month")
    public ResponseEntity<?> monthStatus(
            Authentication auth,
            @RequestParam("classId") int classId,
            @RequestParam("yearMonth") String yearMonth
    ) {
        String teacherId = (String) auth.getPrincipal();
        return ResponseEntity.ok(attendanceService.monthStatus(teacherId, classId, yearMonth));
    }

    
    
    @GetMapping("/teacher/students")
    public ResponseEntity<?> getStudentsWithAttendanceMonth(
            Authentication auth,
            @RequestParam("classId") int classId,
            @RequestParam("yearMonth") String yearMonth
    ) {
        String teacherId = (String) auth.getPrincipal();
        return ResponseEntity.ok(attendanceService.getStudentsOfClassWithMonth(teacherId, classId, yearMonth));
    }


    // ✅ Teacher edit a student's month row
    @PutMapping("/teacher/{studentId}/{yearMonth}")
    public ResponseEntity<?> updateSingle(
            Authentication auth,
            @RequestParam("classId") int classId,
            @PathVariable String studentId,
            @PathVariable String yearMonth,
            @RequestBody AttendanceUpdateRequest req
    ) {
        String teacherId = (String) auth.getPrincipal();
        return ResponseEntity.ok(attendanceService.updateSingle(teacherId, classId, studentId, yearMonth, req));
    }

    // ✅ Student: totals summary for dashboard
    @GetMapping("/me/summary")
    public ResponseEntity<?> mySummary(Authentication auth) {
        String studentId = (String) auth.getPrincipal();
        return ResponseEntity.ok(attendanceService.getMySummary(studentId));
    }

    // ✅ Student: month-wise list (for chart later)
    @GetMapping("/me/monthly")
    public ResponseEntity<?> myMonthly(Authentication auth) {
        String studentId = (String) auth.getPrincipal();
        return ResponseEntity.ok(attendanceService.getMyMonthly(studentId));
    }
}
