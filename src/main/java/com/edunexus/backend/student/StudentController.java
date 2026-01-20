package com.edunexus.backend.student;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin("*")
public class StudentController {

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private StudentService studentService;

    // ✅ Student self profile (GET)
    @GetMapping("/student/me")
    public ResponseEntity<?> getMe(Authentication auth) {
        String studentId = auth.getPrincipal().toString();
        return ResponseEntity.ok(studentService.getMe(studentId));
    }

    // ✅ Student self update (PUT multipart)
    // IMPORTANT: not "/me" because MeController already uses "/me"
    @PutMapping(value = "/student/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateMe(
            Authentication auth,
            @RequestPart("data") StudentUpdateRequest req,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws Exception {
        String studentId = auth.getPrincipal().toString();
        return ResponseEntity.ok(studentService.updateMe(studentId, req, image));
    }

    // ✅ Teacher/Admin listing students (keep your DTO)
    @GetMapping("/students")
    public List<StudentDTO> getStudents() {
        return studentRepo.findAll()
            .stream()
            .map(s -> new StudentDTO(
                    s.getStud_id(),
                    s.getStud_name(),
                    s.getStud_email(),
                    s.getStud_address(),
                    s.getStud_guardian(),
                    s.getStud_mobile()
            ))
            .toList();
    }
}
