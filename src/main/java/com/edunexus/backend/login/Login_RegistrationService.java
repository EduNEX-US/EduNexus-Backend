package com.edunexus.backend.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.edunexus.backend.auth.JWTService;
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

//    public LoginResponse login(LoginRequest req) {
//
//        Optional<Login> optionalLogin = loginRepo.findById(req.getEduId());
//        if (!optionalLogin.isPresent()) {
//            throw new RuntimeException("User does not exist!");
//        }
//
//        Login login = optionalLogin.get();
//
//        if (!passwordEncoder.matches(req.getEduPassword(), login.getEdu_pass())) {
//            throw new RuntimeException("Incorrect password!");
//        }
//
//        switch (req.getRole()) {
//            case "student":
//                if (!studentRepo.existsById(req.getEduId())) {
//                    throw new RuntimeException("You are not registered as a student.");
//                }
//                break;
//
//            case "teacher":
//                if (!teacherRepo.existsById(req.getEduId())) {
//                    throw new RuntimeException("You are not registered as a teacher.");
//                }
//                break;
//
//            case "admin":
////            	Teacher t = new Teacher();
//            	int isAdmin = req.getRole().equals("admin") ? 1 : 0;
//            	Optional<Teacher> optional = teacherRepo.findByIsAdmin(isAdmin);
//            	if(!optional.isPresent()) {
//            		throw new RuntimeException("You are not registered as an Admin");
//            	}
//                break;
//
//            default:
//                throw new RuntimeException("Invalid role value provided!");
//        }
//        
//        String token = jwtService.generateToken(req.getEduId(), req.getRole());
//
//        return new LoginResponse(token, req.getRole());
//    }

//    public LoginResponse login(LoginRequest req) {
//
//        Login login = loginRepo.findById(req.getEduId())
//            .orElseThrow(() -> new RuntimeException("User does not exist!"));
//
//        if (!passwordEncoder.matches(req.getEduPassword(), login.getEdu_pass())) {
//            throw new RuntimeException("Incorrect password!");
//        }
//
//        // If frontend sends a role, validate it matches DB (optional but good)
//        if (req.getRole() != null && !req.getRole().isBlank()) {
//            if (!login.getRole().equalsIgnoreCase(req.getRole())) {
//                throw new RuntimeException("Role mismatch!");
//            }
//        }
//
//        // Optional: admin verification tied to THIS user
//        if ("admin".equalsIgnoreCase(login.getRole())) {
//            Optional<Teacher> tOpt = teacherRepo.findById(login.getEdu_id());
//            if (tOpt.isEmpty() || tOpt.get().getIsAdmin() != 1) {
//                throw new RuntimeException("You are not registered as an Admin");
//            }
//        }
//
//        String token = jwtService.generateToken(login.getEdu_id(), login.getRole());
//        return new LoginResponse(token, login.getRole());
//    }
    


    public void changePassword(Authentication auth, ChangePasswordRequest req) {

        if (auth == null || auth.getPrincipal() == null) {
            throw new RuntimeException("Unauthorized");
        }

        String eduId = auth.getPrincipal().toString();

        if (req.getOldPassword() == null || req.getOldPassword().isBlank()) {
            throw new RuntimeException("Old password required");
        }
        if (req.getNewPassword() == null || req.getNewPassword().isBlank()) {
            throw new RuntimeException("New password required");
        }
        if (req.getNewPassword().length() < 6) {
            throw new RuntimeException("New password must be at least 6 chars");
        }

        Login login = loginRepo.findById(eduId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(req.getOldPassword(), login.getEdu_pass())) {
            throw new RuntimeException("Old password is incorrect");
        }

        // update password (hashed)
        login.setEdu_pass(passwordEncoder.encode(req.getNewPassword()));

        // turn off mustChangePassword
        login.setMustChangePassword(false);

        loginRepo.save(login);
    }

    public LoginResponse login(LoginRequest req) {

        Login login = loginRepo.findById(req.getEduId())
            .orElseThrow(() -> new RuntimeException("User does not exist!"));

        if (!passwordEncoder.matches(req.getEduPassword(), login.getEdu_pass())) {
            throw new RuntimeException("Incorrect password!");
        }

        if (req.getRole() != null && !req.getRole().isBlank()
                && !login.getRole().equalsIgnoreCase(req.getRole())) {
            throw new RuntimeException("Role mismatch!");
        }

        String token = jwtService.generateToken(login.getEdu_id(), login.getRole());
        return new LoginResponse(token, login.getRole());
    }


}
