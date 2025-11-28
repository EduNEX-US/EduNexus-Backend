package com.edunexus.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfiguration {

	
	@Bean 
	public PasswordEncoder passwordEncoder() { //this will configure the password that user entering into hashed form
		return new BCryptPasswordEncoder();
	}
}
