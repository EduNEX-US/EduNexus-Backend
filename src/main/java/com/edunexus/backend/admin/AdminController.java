package com.edunexus.backend.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.edunexus.backend.admin.CreateStudentRequest;	

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class AdminController {
	
	@Autowired
	private StudentService studentService;
	
	@PostMapping("students/register")
	public ResponseEntity<?> createStudent(@RequestBody CreateStudentRequest req) {
		studentService.createStudent(req);
		return ResponseEntity.ok("Student created successfully");
	}
}
