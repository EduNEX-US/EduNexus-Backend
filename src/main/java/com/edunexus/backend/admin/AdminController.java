package com.edunexus.backend.admin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edunexus.backend.APIResponse;
import com.edunexus.backend.student.CreateStudentRequest;
import com.edunexus.backend.student.StudentService;
import com.edunexus.backend.teacher.CreateTeacherRequest;
import com.edunexus.backend.teacher.TeacherService;	

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class AdminController {
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private TeacherService teacherService;
	
	@PostMapping("student/register")
	public ResponseEntity<?> createStudent(@RequestBody CreateStudentRequest req) {
		studentService.createStudent(req);
		return ResponseEntity.ok(new APIResponse("Student registered successfully"));
	}
	
	@PostMapping("teacher/register")
	public ResponseEntity<?> createTeacher(@RequestBody CreateTeacherRequest req) {
		teacherService.createTeacher(req);
		return ResponseEntity.ok(new APIResponse("Teacher registered successfully"));

	}
	
	@PostMapping("admin/register")
	public ResponseEntity<?> createAdmin(@RequestBody CreateTeacherRequest req) {
		teacherService.createTeacher(req);
		return ResponseEntity.ok(new APIResponse("Teacher with Admin rights created successfully"));

	}
}
