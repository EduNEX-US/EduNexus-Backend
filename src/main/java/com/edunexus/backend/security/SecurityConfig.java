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
            .cors(cors -> {}) // ✅ enable CORS using your CorsConfigurationSource bean
            .authorizeHttpRequests(auth -> auth

                // preflight
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // public uploads (images)
                .requestMatchers(HttpMethod.GET, "/uploads/**").permitAll()

                // auth
                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/change-password").authenticated()

                // ================= MARKS =================
                // student endpoints MUST be above any /marks/** rule
                .requestMatchers(HttpMethod.GET, "/marks/me").hasRole("STUDENT")
                .requestMatchers(HttpMethod.GET, "/marks/me/latest").hasRole("STUDENT")

                // teacher endpoints
                .requestMatchers(HttpMethod.GET, "/marks/teacher").hasRole("TEACHER")
                .requestMatchers(HttpMethod.POST, "/marks/manual").hasRole("TEACHER")
                .requestMatchers(HttpMethod.POST, "/marks/csv").hasRole("TEACHER")
                .requestMatchers(HttpMethod.DELETE, "/marks/**").hasRole("TEACHER")

                // optional fallback: any other marks endpoint -> teacher only
                .requestMatchers("/marks/**").hasRole("TEACHER")

                // ================= STUDENTS =================
                // teacher can list/create students (keep if needed)
                .requestMatchers(HttpMethod.GET, "/students").hasAnyRole("TEACHER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/students").hasRole("TEACHER")

                // student self profile
                .requestMatchers(HttpMethod.GET, "/student/me").hasRole("STUDENT")
                .requestMatchers(HttpMethod.PUT, "/student/me").hasRole("STUDENT")

                // ================= ADMIN =================
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/admin/teacher/**").hasRole("ADMIN")

                // ================= TEACHER =================
                .requestMatchers("/teacher/students/**").hasRole("TEACHER")
                .requestMatchers("/teacher/**").hasAnyRole("TEACHER", "ADMIN")

                // ================= /me (LOGIN ROLE INFO) =================
                // this is your MeController endpoint
                .requestMatchers(HttpMethod.GET, "/me").authenticated()

                // ================= LOST & FOUND =================
                .requestMatchers(HttpMethod.GET, "/lostfound/items").hasAnyRole("STUDENT", "TEACHER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/lostfound/items/*/claim").hasRole("STUDENT")

                .requestMatchers(HttpMethod.GET, "/lostfound/claims/pending").hasRole("TEACHER")
                .requestMatchers(HttpMethod.PUT, "/lostfound/claims/*/approve").hasRole("TEACHER")
                .requestMatchers(HttpMethod.PUT, "/lostfound/claims/*/reject").hasRole("TEACHER")

                .requestMatchers(HttpMethod.GET, "/attendance/teacher/students").hasRole("TEACHER")

             // ----- ATTENDANCE -----
                .requestMatchers(HttpMethod.POST, "/attendance/csv").hasRole("TEACHER")
                .requestMatchers(HttpMethod.GET,  "/attendance/teacher").hasRole("TEACHER")
                .requestMatchers(HttpMethod.PUT,  "/attendance/teacher/**").hasRole("TEACHER")

                .requestMatchers(HttpMethod.GET, "/attendance/me/**").hasRole("STUDENT")

                // everything else
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
