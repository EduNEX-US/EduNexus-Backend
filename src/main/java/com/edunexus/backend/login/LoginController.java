package com.edunexus.backend.login;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.edunexus.backend.login.DTO.LoginRequest;
import com.edunexus.backend.login.DTO.LoginResponse;

@RestController
@CrossOrigin("*")
public class LoginController {
	@Autowired
	private Login_RegistrationService loginService;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest req) {
	    try {
	        LoginResponse response = loginService.login(req);
	        return ResponseEntity.ok(response);
	    } 
	    catch (Exception e) {
	        return ResponseEntity.status(401)
	                .body(Map.of("error", e.getMessage())); // return real message
	    }
	}

}
