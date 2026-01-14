package com.edunexus.backend.student;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edunexus.backend.login.Login;
import com.edunexus.backend.login.LoginRepository;


@Service
public class StudentService {
	@Autowired
	private LoginRepository loginRepo;
	
	@Autowired 
	private StudentRepository studentRepo;
	
	@Autowired 
	private PasswordEncoder passwordEncoder;

	@Transactional
	public void createStudent(CreateStudentRequest req) {

	    String studentId = "STD" + UUID.randomUUID().toString().substring(0,8).toUpperCase();

	    String hashedPassword = passwordEncoder.encode(req.getStudentPassword());

	    Login login = new Login();
	    login.setEdu_id(studentId);
	    login.setEdu_pass(hashedPassword);
	    login.setRole("student");   // 🔥 FORCE ROLE

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