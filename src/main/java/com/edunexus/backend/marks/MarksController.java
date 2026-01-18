package com.edunexus.backend.marks;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/marks")
@CrossOrigin("*")
public class MarksController {

    @Autowired private MarksService marksService;

    @PostMapping("/manual")
    public ResponseEntity<?> uploadManual(Authentication auth, @RequestBody MarksManualRequest req) {
        String teacherId = (String) auth.getPrincipal();
        return ResponseEntity.ok(marksService.uploadManual(teacherId, req));
    }

    @PostMapping(value = "/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadCsv(
            Authentication auth,
            @RequestParam("classId") int classId,
            @RequestParam("examSession") String examSession,
            @RequestParam("resultDate") String resultDate,
            @RequestParam("file") MultipartFile file
    ) throws Exception {
        String teacherId = (String) auth.getPrincipal();
        return ResponseEntity.ok(marksService.uploadCsv(teacherId, classId, examSession, resultDate, file));
    }

    @GetMapping("/teacher")
    public ResponseEntity<?> getForTeacher(
            Authentication auth,
            @RequestParam("classId") int classId,
            @RequestParam("examSession") String examSession
    ) {
        String teacherId = (String) auth.getPrincipal();
        List<Marks> rows = marksService.getMarksForClassSession(teacherId, classId, examSession);
        return ResponseEntity.ok(rows);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getForStudent(
            Authentication auth,
            @RequestParam("examSession") String examSession
    ) {
        String studentId = (String) auth.getPrincipal();
        return ResponseEntity.ok(marksService.getMyMarksForSession(studentId, examSession));
    }

    // ✅ ADD THIS
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMarks(Authentication auth, @PathVariable Long id) {
        String teacherId = (String) auth.getPrincipal();
        marksService.deleteMarksById(teacherId, id);
        return ResponseEntity.ok().build();
    }
}
