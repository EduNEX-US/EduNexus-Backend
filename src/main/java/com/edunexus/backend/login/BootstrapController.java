package com.edunexus.backend.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bootstrap")
@Profile("dev")   // ONLY works in dev mode
public class BootstrapController {

    @Autowired LoginRepository loginRepo;
    @Autowired PasswordEncoder encoder;

    @PostMapping
    public String createDemoUsers() {

        if (loginRepo.count() > 0) {
            return "Already initialized";
        }

        loginRepo.save(new Login("admin01", encoder.encode("admin123"), "admin"));
        loginRepo.save(new Login("teacher01", encoder.encode("teach123"), "teacher"));
        loginRepo.save(new Login("student01", encoder.encode("stud123"), "student"));

        return "Demo users created";
    }
}
