package com.edunexus.backend.teacher;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.edunexus.backend.login.Login;
import com.edunexus.backend.login.LoginRepository;

@Service
public class TeacherService {
	@Autowired
	private TeacherRepository teacherRepo;
	
	@Autowired
	private LoginRepository loginRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public void createTeacher(CreateTeacherRequest req) {
		String hashedPassword = passwordEncoder.encode(req.getPassword());
		
		String teacherId = "STD"+UUID.randomUUID().toString().substring(0, 8).toUpperCase(); // a random ID 
		
		//Register teacher and save the hashed Password in login table. Later used for validation
	
//		if(req.getIsAdmin().equals("teacher")) {
//			teacher.setIsAdmin(1);
//		} else {
//			teacher.setIsAdmin(0);
//		}
		
		
		Login login = new Login();
		login.setEdu_id(teacherId);
		login.setEdu_pass(hashedPassword);
		loginRepo.save(login);
		
		Teacher teacher = new Teacher();
		
		teacher.setTeacher_id(teacherId);
		teacher.setTeacher_name(req.getTeacherName());
		teacher.setTeacher_email(req.getTeacherEmail());
		teacher.setTeacher_mob(req.getTeacherMob());
		teacher.setTeacher_add(req.getTeacherAdd());
		teacher.setTeacher_exp(req.getTeacherExp());
		teacher.setTeacher_class(req.getTeacherClass());
		String role = req.getRole();
		int admin = (role.equals("admin")) ? 1 : 0;
		teacher.setIsAdmin(admin);
		teacher.setTeacher_qualification(req.getTeacherQualification());
		teacher.setPassword(req.getPassword());
		teacherRepo.save(teacher);
		
	}
}
