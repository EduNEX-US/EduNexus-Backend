package com.edunexus.backend.admin;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.edunexus.backend.teacher.CreateTeacherRequest;
import com.edunexus.backend.teacher.Teacher;
import com.edunexus.backend.teacher.TeacherDTO;
import com.edunexus.backend.teacher.TeacherImageService;
import com.edunexus.backend.teacher.TeacherRepository;
import com.edunexus.backend.teacher.TeacherService;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class AdminController {

    private static final String DEFAULT_PASSWORD = "Welcom@123";

    @Autowired private TeacherService teacherService;
    @Autowired private AdminImportService importService;
    @Autowired private AdminExportService exportService;
    @Autowired private TeacherRepository teacherRepo;
    @Autowired private TeacherImageService teacherImageService;
    @Autowired private AdminImageService adminImageService;
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getTeacherById(@PathVariable String id) {
        try {
            Teacher t = teacherRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

            return ResponseEntity.ok(Map.of(
                "id", t.getTeacher_id(),
                "name", t.getTeacher_name(),
                "email", t.getTeacher_email(),
                "mobile", t.getTeacher_mob(),
                "exp", t.getTeacher_exp(),
                "address", t.getTeacher_add(),
                "tClass", t.getTeacher_class(),
                "qualification", t.getTeacher_qualification(),
                "imageUrl", t.getImageUrl() == null ? "" : t.getImageUrl()
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Error fetching teacher"));
        }
    }
    
    @PutMapping("/profile")
    public ResponseEntity<?> updateMyAdminProfile(
            @RequestBody AdminProfileUpdateRequest req,
            Authentication auth
    ) {
        try {
            String adminId = auth.getName();

            Teacher t = teacherRepo.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

            if (t.getIsAdmin() != 1) {
                return ResponseEntity.status(403).body(Map.of("error", "Not an admin"));
            }

            t.setTeacher_name(req.getName());
            t.setTeacher_email(req.getEmail());
            t.setTeacher_mob(req.getMobile());
            t.setTeacher_exp(req.getExp());
            t.setTeacher_add(req.getAddress());
            t.setTeacher_qualification(req.getQualification());

            teacherRepo.save(t);

            return ResponseEntity.ok(Map.of("message", "Profile updated"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Update failed"));
        }
    }

    @PostMapping(value = "/profile/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadMyAdminImage(
            @RequestPart("image") MultipartFile image,
            Authentication auth
    ) {
        try {
            String adminId = auth.getName();

            Teacher t = teacherRepo.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

            if (t.getIsAdmin() != 1) {
                return ResponseEntity.status(403).body(Map.of("error", "Not an admin"));
            }

            // delete old
            if (t.getImageUrl() != null && !t.getImageUrl().isBlank()) {
                adminImageService.deleteAdminImageByUrl(t.getImageUrl());
            }

            // save new
            String url = adminImageService.saveAdminImage(image);
            t.setImageUrl(url);
            teacherRepo.save(t);

            return ResponseEntity.ok(Map.of("message", "Image updated", "imageUrl", url));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Upload failed"));
        }
    }

    @PostMapping("/teacher/register")
    public ResponseEntity<?> createTeacher(@RequestBody CreateTeacherRequest req) {
        req.setRole("teacher");
        req.setPassword(DEFAULT_PASSWORD);
        String id = teacherService.createTeacherAndReturnId(req);

        return ResponseEntity.ok(
            java.util.Map.of(
                "message", "Teacher registered successfully",
                "id", id,
                "defaultPassword", DEFAULT_PASSWORD
            )
        );
    }

    @PostMapping("/admin/register")
    public ResponseEntity<?> createAdmin(@RequestBody CreateTeacherRequest req) {
        req.setRole("admin");
        req.setPassword(DEFAULT_PASSWORD);
        String id = teacherService.createTeacherAndReturnId(req);

        return ResponseEntity.ok(
            java.util.Map.of(
                "message", "Admin registered successfully",
                "id", id,
                "defaultPassword", DEFAULT_PASSWORD
            )
        );
    }

    @PostMapping("/teacher/import-csv")
    public ResponseEntity<?> importTeachersCsv(@RequestParam("file") MultipartFile file) {
        ImportResult result = importService.importTeachersCsv(file, DEFAULT_PASSWORD);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/teacher/export-csv")
    public ResponseEntity<ByteArrayResource> exportTeachersCsv() {
        String csv = exportService.exportTeachersCsv(DEFAULT_PASSWORD);
        byte[] bytes = csv.getBytes(StandardCharsets.UTF_8);

        ByteArrayResource resource = new ByteArrayResource(bytes);

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=teachers_export.csv")
            .contentType(MediaType.parseMediaType("text/csv"))
            .contentLength(bytes.length)
            .body(resource);
    }

    // JSON list for table
    @GetMapping("/teachers")
    public List<TeacherDTO> listTeachers() {
        return teacherRepo.findAll()
            .stream()
            .map(t -> new TeacherDTO(t.getTeacher_id(), t.getTeacher_name(), t.getIsAdmin()))
            .toList();
    }
    
    @DeleteMapping("/teacher/{teacherId}")
    public ResponseEntity<?> deleteTeacher(@PathVariable String teacherId) {
    	Teacher t = teacherRepo.findById(teacherId).orElse(null);
    	if (t != null && t.getIsAdmin() == 1) {
    	   return ResponseEntity.status(403).body(Map.of("error", "Cannot delete admin teacher"));
    	}

        teacherService.deleteTeacherById(teacherId);
        return ResponseEntity.ok(java.util.Map.of(
            "message", "Teacher deleted successfully",
            "id", teacherId
        ));
    }
    
    @PostMapping(value = "/teacher/register-with-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createTeacherWithImage(
            @RequestPart("data") CreateTeacherRequest req,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        try {
            req.setRole("teacher");
            req.setPassword(DEFAULT_PASSWORD);

            if (image != null && !image.isEmpty()) {
                String imageUrl = teacherImageService.saveTeacherImage(image); // ✅ use your injected name
                req.setImageUrl(imageUrl);
            }

            String id = teacherService.createTeacherAndReturnId(req);

            return ResponseEntity.ok(Map.of(
                    "message", "Teacher registered successfully",
                    "id", id,
                    "defaultPassword", DEFAULT_PASSWORD
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Error creating teacher"));
        }
    }


}
