package com.edunexus.backend.teacher;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.edunexus.backend.APIResponse;
import com.edunexus.backend.student.CreateStudentRequest;
import com.edunexus.backend.student.StudentService;

@RestController
@CrossOrigin("*")
@RequestMapping("/teacher")
public class TeacherController {
	@Autowired
    private TeacherRepository teacherRepo;

	 @Autowired private StudentService studentService;

	 @Autowired private TeacherImageService teacherImageService;
	 


	 @PostMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	    public ResponseEntity<?> uploadMyImage(
	            @RequestParam("teacherId") String teacherId,
	            @RequestPart("image") MultipartFile image
	    ) {
	        try {
	            Teacher t = teacherRepo.findById(teacherId)
	                    .orElseThrow(() -> new RuntimeException("Teacher not found"));

	            // delete old if exists
	            if (t.getImageUrl() != null && !t.getImageUrl().isBlank()) {
	                teacherImageService.deleteTeacherImageByUrl(t.getImageUrl());
	            }

	            // save new
	            String imageUrl = teacherImageService.saveTeacherImage(image);
	            t.setImageUrl(imageUrl);
	            teacherRepo.save(t);

	            return ResponseEntity.ok(Map.of("message", "Image updated", "imageUrl", imageUrl));
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(500).body(Map.of("error", "Upload failed"));
	        }
	    }

    @PostMapping("/students")
    public ResponseEntity<?> createStudent(@RequestBody CreateStudentRequest req) {
        studentService.createStudent(req); // sets role=student internally
        return ResponseEntity.ok(new APIResponse("Student created successfully"));
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
