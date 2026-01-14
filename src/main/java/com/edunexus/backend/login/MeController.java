package com.edunexus.backend.login;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeController {

    @Autowired
    private LoginRepository loginRepo;

    @GetMapping("/me")
    public Map<String, Object> me(Authentication auth) {

        // If token invalid, Spring Security will block before reaching here
        String eduId = auth.getPrincipal().toString();

        Login login = loginRepo.findById(eduId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        return Map.of(
            "id", login.getEdu_id(),
            "role", login.getRole(),
            "mustChangePassword", login.isMustChangePassword()
        );
    }
}
