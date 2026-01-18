package com.edunexus.backend.attendance;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/teacher/attendance")
@CrossOrigin("*")
public class AttendanceController {

    @Autowired private AttendanceService attendanceService;

    private String currentUserId() {
        Object p = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return String.valueOf(p);
    }

    
    @GetMapping("/class/{classId}")
    public ResponseEntity<?> getForClass(
            @PathVariable int classId,
            @RequestParam(required = false) String date
    ) {
        try {
            LocalDate d = (date == null || date.isBlank()) ? LocalDate.now() : LocalDate.parse(date);
            Map<String, Object> result = attendanceService.getAttendanceForClassByDate(classId, d);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error fetching attendance"));
        }
    }
    // ✅ CSV upload: replace for date+class
    @PostMapping(value="/upload-csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadCsv(
            @RequestParam("classId") int classId,
            @RequestParam("date") String date, // yyyy-MM-dd
            @RequestParam("file") MultipartFile file
    ) {
        try {
            String teacherId = currentUserId();
            AttendanceUploadResult result = attendanceService.uploadAbsenteesCsv(
                    teacherId, classId, LocalDate.parse(date), file
            );
            return ResponseEntity.ok(Map.of("message", "Uploaded", "result", result));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Manual upload: replace for date+class
    @PostMapping(value="/upload-manual", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadManual(@RequestBody AttendanceManualRequest req) {
        try {
            String teacherId = currentUserId();
            AttendanceUploadResult result = attendanceService.uploadManual(teacherId, req);
            return ResponseEntity.ok(Map.of("message", "Uploaded", "result", result));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}
