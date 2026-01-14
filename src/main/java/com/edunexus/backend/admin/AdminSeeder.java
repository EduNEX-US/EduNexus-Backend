package com.edunexus.backend.admin;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.edunexus.backend.login.Login;
import com.edunexus.backend.login.LoginRepository;
import com.edunexus.backend.teacher.Teacher;
import com.edunexus.backend.teacher.TeacherRepository;

@Configuration
public class AdminSeeder {

    @Bean
    CommandLineRunner seedAdmins(
            LoginRepository loginRepo,
            TeacherRepository teacherRepo,
            PasswordEncoder encoder
    ) {
        return args -> {

            seedOneAdmin(loginRepo, teacherRepo, encoder,
                "ADM001", "Admin@123",
                "Dr. Kuldeep Malhotra",
                "admin1@school.com",
                9999999991L,
                "Head Office",
                10,
                "ALL",
                "PhD Education");

            seedOneAdmin(loginRepo, teacherRepo, encoder,
                "ADM002", "Admin@123",
                "Mrs. Suman Sharma",
                "admin2@school.com",
                9999999992L,
                "Branch Office",
                8,
                "ALL",
                "M.A B.Ed");
        };
    }

    private void seedOneAdmin(
            LoginRepository loginRepo,
            TeacherRepository teacherRepo,
            PasswordEncoder encoder,
            String adminId,
            String rawPassword,
            String name,
            String email,
            long mobile,
            String address,
            int experience,
            String teacherClass,
            String qualification
    ) {

        // --- Create/Update Login row ---
        if (!loginRepo.existsById(adminId)) {
            Login login = new Login();
            login.setEdu_id(adminId);
            login.setEdu_pass(encoder.encode(rawPassword));
            login.setRole("admin");
            login.setMustChangePassword(true); // ✅ boolean
            loginRepo.save(login);
        }

        // --- Create/Update Teacher row ---
        if (!teacherRepo.existsById(adminId)) {
            Teacher teacher = new Teacher();
            teacher.setTeacher_id(adminId);
            teacher.setTeacher_name(name);
            teacher.setTeacher_email(email);
            teacher.setTeacher_mob(mobile);
            teacher.setTeacher_add(address);
            teacher.setTeacher_exp(experience);
            teacher.setTeacher_class(teacherClass);
            teacher.setTeacher_qualification(qualification);

            teacher.setIsAdmin(1); // ✅ Admin-Teacher marker
            teacherRepo.save(teacher);
        }

        System.out.println("✅ Admin seeded: " + adminId);
    }
}
