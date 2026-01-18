package com.edunexus.backend.teacher;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import org.springframework.web.server.ResponseStatusException;

import com.edunexus.backend.admin.ImportResult;
import com.edunexus.backend.student.CreateStudentRequest;
import com.edunexus.backend.student.StudentDTO;
import com.edunexus.backend.student.StudentImageService;
import com.edunexus.backend.student.StudentService;

@RestController
@CrossOrigin("*")
@RequestMapping("/teacher")
public class TeacherController {
	
	private static final String DEFAULT_PASSWORD = "Welcom@123";
	
	@Autowired
    private TeacherRepository teacherRepo;

	 @Autowired private StudentService studentService;

	 @Autowired private TeacherImageService teacherImageService;
	 
	 @Autowired private StudentImageService studentImageService;

	 @Autowired private TeacherStudentImportService importService;
	 
	 @Autowired
	 private StudentExportService studentExportService;

	 @GetMapping("/student/export-csv")
	 public ResponseEntity<ByteArrayResource> exportMyStudentsCsv(Authentication auth) {

	     // logged-in teacher id
	     String teacherId = auth.getName();

	     Teacher teacher = teacherRepo.findById(teacherId)
	         .orElseThrow(() -> new RuntimeException("Teacher not found"));

	     int teacherClass = teacher.getTeacher_class();

	     String csv = studentExportService.exportStudentsCsv(teacherClass);
	     byte[] bytes = csv.getBytes(java.nio.charset.StandardCharsets.UTF_8);

	     ByteArrayResource resource = new ByteArrayResource(bytes);

	     return ResponseEntity.ok()
	         .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=students_export.csv")
	         .contentType(MediaType.parseMediaType("text/csv"))
	         .contentLength(bytes.length)
	         .body(resource);
	 }

	 @GetMapping("/students/class/{classId}")
	    public List<StudentDTO> getStudentsByClass(@PathVariable int classId) {
	        return studentService.getStudentsByClass(classId);
	    }

	    @PostMapping("/students/import-csv")
	    public ResponseEntity<?> importMyStudentsCsv(@RequestParam("file") MultipartFile file) {
	        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        String teacherId = (String) auth.getPrincipal();

	        ImportResult result = importService.importMyStudentsCsv(teacherId, file);
	        return ResponseEntity.ok(result);
	    }
	    
	    @PutMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	    public ResponseEntity<?> updateMyProfile(
	            Authentication auth,
	            @RequestPart("data") TeacherUpdateRequest req,
	            @RequestPart(value = "image", required = false) MultipartFile image
	    ) {
	        try {
	            // ✅ teacherId from JWT (same style you used in export)
	            String teacherId = auth.getName();

	            Teacher t = teacherRepo.findById(teacherId)
	                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found"));

	            // ✅ Update basic fields (only if provided)
	            if (req.getName() != null) t.setTeacher_name(req.getName());
	            if (req.getEmail() != null) t.setTeacher_email(req.getEmail());
	            if (req.getMobile() != null) t.setTeacher_mob(req.getMobile());
	            if (req.getExp() != null) t.setTeacher_exp(req.getExp());
	            if (req.getAddress() != null) t.setTeacher_add(req.getAddress());
	            if (req.getQualification() != null) t.setTeacher_qualification(req.getQualification());
	            if (req.getTClass() != null) t.setTeacher_class(req.getTClass());

	            // ✅ If image uploaded, replace old image
	            if (image != null && !image.isEmpty()) {
	                if (t.getImageUrl() != null && !t.getImageUrl().isBlank()) {
	                    teacherImageService.deleteTeacherImageByUrl(t.getImageUrl());
	                }
	                String imageUrl = teacherImageService.saveTeacherImage(image);
	                t.setImageUrl(imageUrl);
	            }

	            teacherRepo.save(t);

	            // ✅ return updated profile in SAME SHAPE as GET /teacher/{id}
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

	        } catch (ResponseStatusException ex) {
	            throw ex;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(500).body(Map.of("error", "Profile update failed"));
	        }
	    }

	    
	    @PostMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	    public ResponseEntity<?> uploadMyImage(Authentication auth,
	            @RequestPart("image") MultipartFile image
	    ) {
	        try {
	            String teacherId = auth.getName();

	            Teacher t = teacherRepo.findById(teacherId)
	                    .orElseThrow(() -> new RuntimeException("Teacher not found"));

	            if (t.getImageUrl() != null && !t.getImageUrl().isBlank()) {
	                teacherImageService.deleteTeacherImageByUrl(t.getImageUrl());
	            }

	            String imageUrl = teacherImageService.saveTeacherImage(image);
	            t.setImageUrl(imageUrl);
	            teacherRepo.save(t);

	            return ResponseEntity.ok(Map.of("message", "Image updated", "imageUrl", imageUrl));
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(500).body(Map.of("error", "Upload failed"));
	        }
	    }


	 
	 @PostMapping(value="/student/register-with-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	 public ResponseEntity<?> createStudent(
	         @RequestPart("data") CreateStudentRequest req,
	         @RequestPart(value="image", required=false) MultipartFile image
	 ){
	     try {
	         String imageUrl = "";
	         if (image != null && !image.isEmpty()) {
	             imageUrl = studentImageService.saveStudentImage(image);
	             req.setImageUrl(imageUrl);
	         }
	         req.setStudentPassword(DEFAULT_PASSWORD);
	         studentService.createStudent(req); // should save imageUrl in DB
	         return ResponseEntity.ok(Map.of("message","Student created", "imageUrl", imageUrl));
	     } catch(Exception e) {
	         e.printStackTrace();
	         return ResponseEntity.status(500).body(Map.of("error","Student create failed"));
	     }
	 }

	 @PostMapping("/student/register")
	    public ResponseEntity<?> createStudent(@RequestBody CreateStudentRequest req) {
	        req.setRole("teacher");
	        req.setStudentPassword(DEFAULT_PASSWORD);
	        String id = studentService.createStudentAndReturnId(req);

	        return ResponseEntity.ok(
	            java.util.Map.of(
	                "message", "Student registered successfully",
	                "id", id,
	                "defaultPassword", DEFAULT_PASSWORD
	            )
	        );
	    }
	
	 @DeleteMapping("/students/{studentId}")
	 public ResponseEntity<?> deleteStudent(@PathVariable String studentId) {
	     try {
	         studentService.deleteStudentById(studentId);
	         return ResponseEntity.ok(
	             Map.of("message", "Student deleted successfully", "id", studentId)
	         );
	     } catch (Exception e) {
	         return ResponseEntity.status(404)
	             .body(Map.of("error", e.getMessage()));
	     }
	 }

    @GetMapping("/teacherNames")
    public List<TeacherDTO> getTeacherNames() {
       return teacherRepo.findAll()
       .stream()
       .filter(t -> t.getIsAdmin() == 0)   // ❌ exclude admins
       .map(t -> new TeacherDTO(
               t.getTeacher_id(),
               t.getTeacher_name(),
               t.getIsAdmin()
       ))
       .toList();
    }

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

}
