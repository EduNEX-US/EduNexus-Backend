package com.edunexus.backend.admin;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.edunexus.backend.auth.JWTService;
import com.edunexus.backend.login.Login;
import com.edunexus.backend.login.LoginRepository;
import com.edunexus.backend.student.Student;
import com.edunexus.backend.student.StudentRepository;

@Service
public class StudentService {
	@Autowired
	private LoginRepository loginRepo;
	
	@Autowired 
	private StudentRepository studentRepo;
	
	@Autowired 
	private PasswordEncoder passwordEncoder;

	
	public void createStudent(CreateStudentRequest req) {
		String hashedPassword = passwordEncoder.encode(req.getStudentPassword());
		
		//Also store the details in students repository because registration for student
		//1- Once registered we add to the login table
		
		String studentId = "STD"+UUID.randomUUID().toString().substring(0, 8).toUpperCase();
		
		//save the hashed password in login table
				Login login = new Login();
				login.setEdu_id(studentId);
				login.setEdu_pass(hashedPassword);
				loginRepo.save(login);
				
				Student student = new Student();	
				
				student.setStud_id(studentId);
				student.setStud_name(req.getStudentName());
				student.setStud_email(req.getStudentEmail());
				student.setStud_mobile(req.getStudentMobile());
				student.setStud_class(req.getStudentClass());
				student.setStud_address(req.getStudentAddress());
				student.setStud_alt_mob(req.getStudentAltMobile());
				student.setStud_guardian(req.getStudentGuardian());
				
				student.setbasicFee(req.getBasicFee());	
				
				studentRepo.save(student);
	}
}
