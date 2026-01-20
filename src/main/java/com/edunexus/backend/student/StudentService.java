package com.edunexus.backend.student;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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


    @Autowired private StudentImageService studentImageService;

    public StudentMeResponse getMe(String studentId) {
        Student s = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("STUDENT_NOT_FOUND"));

        return new StudentMeResponse(
                s.getStud_id(),
                s.getStud_name(),
                s.getStud_email(),
                s.getStud_mobile(),
                s.getStud_class(),
                s.getStud_address(),
                s.getStud_alt_mob(),
                s.getStud_guardian(),
                s.getImageUrl() == null ? "" : s.getImageUrl()
        );
    }

    @Transactional
    public StudentMeResponse updateMe(String studentId, StudentUpdateRequest req, MultipartFile image) throws Exception {
        Student s = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("STUDENT_NOT_FOUND"));

        if (req.getName() != null) s.setStud_name(req.getName().trim());
        if (req.getEmail() != null) s.setStud_email(req.getEmail().trim());
        if (req.getMobile() != null) s.setStud_mobile(req.getMobile());
        if (req.getAddress() != null) s.setStud_address(req.getAddress().trim());

        if (image != null && !image.isEmpty()) {
            String url = studentImageService.saveStudentImage(image);
            s.setImageUrl(url);
        }

        studentRepo.save(s);
        return getMe(studentId);
    }
	public List<StudentDTO> getStudentsByClass(int studClass) {
	    return studentRepo.findByStudClass(studClass)
	            .stream()
	            .map(s -> new StudentDTO(
	                    s.getStud_id(),
	                    s.getStud_name(),
	                    s.getStud_email(),
	                    s.getStud_address(),
	                    s.getStud_guardian(),
	                    s.getStud_mobile()
	            ))
	            .toList();
	}

	@Transactional
	public void deleteStudentById(String studentId) {
	    if (!studentRepo.existsById(studentId)) {
	        throw new RuntimeException("Student not found");
	    }
	    studentRepo.deleteById(studentId);
	}

    
	@Transactional
	public void createStudent(CreateStudentRequest req) {

	    String studentId = "STD" + UUID.randomUUID().toString().substring(0,8).toUpperCase();

	    String hashedPassword = passwordEncoder.encode(req.getStudentPassword());

	    Login login = new Login();
	    login.setEdu_id(studentId);
	    login.setEdu_pass(hashedPassword);
	    login.setRole("student");   // 🔥 FORCE ROLE
	    login.setMustChangePassword(true);
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

	    studentRepo.save(student);
	}
	
	@Transactional
    public String createStudentAndReturnId(CreateStudentRequest req) {

        String studentId = "STD" + UUID.randomUUID().toString().substring(0,8).toUpperCase();

        String hashedPassword = passwordEncoder.encode(req.getStudentPassword());

        // normalize role safely
        String role = (req.getRole() == null) ? "student" : req.getRole().trim().toLowerCase();

        // 1) Save login row
        Login login = new Login();
        login.setEdu_id(studentId);
        login.setEdu_pass(hashedPassword);
        login.setRole("student");
        login.setMustChangePassword(true); // ✅ force change on first login
        loginRepo.save(login);

        // 2) Save teacher profile
        Student student = new Student();
        student.setStud_id(studentId);
        student.setStud_name(req.getStudentName());
        student.setStud_email(req.getStudentEmail());
        student.setStud_mobile(req.getStudentMobile());
        student.setStud_alt_mob(req.getStudentAltMobile());
        student.setStud_guardian(req.getStudentGuardian());
        student.setStud_class(req.getStudentClass());
        student.setStud_address(req.getStudentAddress());
        student.setImageUrl(req.getImageUrl());
        studentRepo.save(student);
        return studentId;
    }

}