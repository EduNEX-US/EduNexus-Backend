package com.edunexus.backend.login;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.edunexus.backend.auth.JWTService;
import com.edunexus.backend.config.SecurityConfiguration;
import com.edunexus.backend.login.DTO.LoginRequest;
import com.edunexus.backend.login.DTO.LoginResponse;
import com.edunexus.backend.student.StudentRepository;
import com.edunexus.backend.teacher.TeacherRepository;

@Service
public class Login_RegistrationService {

    @Autowired
    private LoginRepository loginRepo;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private TeacherRepository teacherRepo;
    
	
	@Autowired 
	private PasswordEncoder passwordEncoder;
    @Autowired 
    private JWTService jwtService;

    public LoginResponse login(LoginRequest req) {

        Optional<Login> optionalLogin = loginRepo.findById(req.getEduId());
        if (!optionalLogin.isPresent()) {
            throw new RuntimeException("User does not exist!");
        }

        Login login = optionalLogin.get();

        if (!passwordEncoder.matches(req.getEduPassword(), login.getEdu_pass())) {
            throw new RuntimeException("Incorrect password!");
        }

        switch (req.getRole()) {
            case "student":
                if (!studentRepo.existsById(req.getEduId())) {
                    throw new RuntimeException("You are not registered as a student.");
                }
                break;

            case "teacher":
                if (!teacherRepo.existsById(req.getEduId())) {
                    throw new RuntimeException("You are not registered as a teacher.");
                }
                break;

            case "admin":
                break;

            default:
                throw new RuntimeException("Invalid role value provided!");
        }
        
        String token = jwtService.generateToken(req.getEduId(), req.getRole());

        return new LoginResponse(token, req.getRole());
    }

}
