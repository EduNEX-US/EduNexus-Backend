package com.edunexus.backend.adminview;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edunexus.backend.teacher.Teacher;
import com.edunexus.backend.teacher.TeacherRepository;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminViewController {
	@Autowired
    private TeacherRepository teacherRepo;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {

        // For now, assuming only ONE admin exists
        Teacher admin = teacherRepo.findByIsAdmin(1)
                .orElse(null);

        if (admin == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Admin not found"));
        }

        AdminProfileResponse res = new AdminProfileResponse();
        res.setName(admin.getTeacher_name());
        res.setEmail(admin.getTeacher_email());
        res.setMobile(admin.getTeacher_mob());
        res.setRole("Admin");
        res.setAddress(admin.getTeacher_add());

        return ResponseEntity.ok(res);
}
}