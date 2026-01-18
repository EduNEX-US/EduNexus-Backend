package com.edunexus.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> {})   // ✅ enable CORS using your CorsConfigurationSource bean
            .authorizeHttpRequests(auth -> auth
            	    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

            	    // ✅ allow image files to be fetched publicly
            	    .requestMatchers(HttpMethod.GET, "/uploads/**").permitAll()
            	    
            	    // only login is public
            	    .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()

            	    // change password must be authenticated
            	    .requestMatchers(HttpMethod.POST, "/auth/change-password").authenticated()

            	    .requestMatchers("/marks/**").hasRole("TEACHER")

            	    .requestMatchers(HttpMethod.POST, "/students").hasRole("TEACHER")

            	    .requestMatchers(HttpMethod.DELETE, "/marks/**").hasRole("TEACHER")

            	    // admin endpoints
            	    .requestMatchers("/admin/**").hasRole("ADMIN")
            	    
            	 // ✅ allow teacher to import/export students
                    .requestMatchers("/teacher/students/**").hasRole("TEACHER")

            	    .requestMatchers(HttpMethod.DELETE, "/admin/teacher/**").hasRole("ADMIN")

            	    .requestMatchers("/teacher/**").hasAnyRole("TEACHER", "ADMIN")
            	    // me must be authenticated
            	    .requestMatchers("/me").authenticated()

            	    .anyRequest().authenticated()
            	)

            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .build();   // ✅ REQUIRED
    }



}
