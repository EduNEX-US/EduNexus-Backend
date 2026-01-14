package com.edunexus.backend.teacher;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edunexus.backend.APIResponse;
import com.edunexus.backend.student.CreateStudentRequest;
import com.edunexus.backend.student.StudentDTO;
import com.edunexus.backend.student.StudentService;

@RestController
@CrossOrigin("*")
public class TeacherController {
	@Autowired
    private TeacherRepository teacherRepo;

	 @Autowired private StudentService studentService;

    @PostMapping("/students")
    public ResponseEntity<?> createStudent(@RequestBody CreateStudentRequest req) {
        studentService.createStudent(req); // sets role=student internally
        return ResponseEntity.ok(new APIResponse("Student created successfully"));
    }
	
	@GetMapping("/teacherNames")
    public List<TeacherDTO> getStudents() {
       return teacherRepo.findAll()
       .stream()
       .map(student -> new TeacherDTO(
               student.getTeacher_id(),
               student.getTeacher_name(),
               student.getIsAdmin()
       ))
       .toList();
    }
}
