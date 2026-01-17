package com.edunexus.backend.teacher;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.edunexus.backend.login.Login;
import com.edunexus.backend.login.LoginRepository;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepo;

    @Autowired
    private LoginRepository loginRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    public List<TeacherListItem> getAllTeachersForAdmin() {
        return teacherRepo.findAll().stream()
            .map(t -> new TeacherListItem(
                t.getTeacher_id(),
                t.getTeacher_name(),
                t.getTeacher_email(),
                t.getTeacher_mob(),
                t.getTeacher_add(),
                t.getTeacher_exp(),
                t.getTeacher_class(),
                t.getTeacher_qualification(),
                t.getIsAdmin()
            ))
            .collect(Collectors.toList());
    }

    // save teacher image in uploads/teachers/
    public String saveTeacherImage(MultipartFile file) throws Exception {
        String original = StringUtils.cleanPath(file.getOriginalFilename());
        String ext = "";
        int dot = original.lastIndexOf('.');
        if (dot >= 0) ext = original.substring(dot).toLowerCase();

        if (!ext.matches("\\.(png|jpg|jpeg|webp)$")) {
            throw new RuntimeException("Only png/jpg/jpeg/webp allowed");
        }

        String filename = UUID.randomUUID().toString() + ext;

        // ✅ store inside uploads/teachers
        Path dirPath = Paths.get(uploadDir).toAbsolutePath().normalize().resolve("teachers");
        Files.createDirectories(dirPath);

        Path target = dirPath.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/teachers/" + filename;
    }

    // delete teacher image file from disk
    public void deleteTeacherImageByUrl(String imageUrl) throws Exception {
        if (imageUrl == null || imageUrl.isBlank()) return;

        // imageUrl: /uploads/teachers/<file>
        String filename = imageUrl.replaceFirst("^/uploads/teachers/", "");

        Path filePath = Paths.get(uploadDir)
                .toAbsolutePath()
                .normalize()
                .resolve("teachers")
                .resolve(filename);

        Files.deleteIfExists(filePath);
    }
    
    @Transactional
    public void createTeacher(CreateTeacherRequest req) {

        String teacherId = "TCH" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        String hashedPassword = passwordEncoder.encode(req.getPassword());

        // normalize role safely
        String role = (req.getRole() == null) ? "teacher" : req.getRole().trim().toLowerCase();
        boolean isAdmin = role.equals("admin");

        // 1) Save login row (ONLY place for password)
        Login login = new Login();
        login.setEdu_id(teacherId);
        login.setEdu_pass(hashedPassword);
        login.setRole(isAdmin ? "admin" : "teacher");
        login.setMustChangePassword(true); // ✅ boolean
        loginRepo.save(login);

        // 2) Save teacher profile row (NO password)
        Teacher teacher = new Teacher();
        teacher.setTeacher_id(teacherId);
        teacher.setTeacher_name(req.getTeacherName());
        teacher.setTeacher_email(req.getTeacherEmail());
        teacher.setTeacher_mob(req.getTeacherMob());
        teacher.setTeacher_add(req.getTeacherAdd());
        teacher.setTeacher_exp(req.getTeacherExp());
        teacher.setTeacher_class(req.getTeacherClass());
        teacher.setTeacher_qualification(req.getTeacherQualification());

        teacher.setIsAdmin(isAdmin ? 1 : 0);
        teacherRepo.save(teacher);
    }
    
    @Transactional
    public String createTeacherAndReturnId(CreateTeacherRequest req) {

        String teacherId = "TCH" + UUID.randomUUID().toString().substring(0,8).toUpperCase();

        String hashedPassword = passwordEncoder.encode(req.getPassword());

        // normalize role safely
        String role = (req.getRole() == null) ? "teacher" : req.getRole().trim().toLowerCase();
        boolean isAdmin = role.equals("admin");

        // 1) Save login row
        Login login = new Login();
        login.setEdu_id(teacherId);
        login.setEdu_pass(hashedPassword);
        login.setRole(isAdmin ? "admin" : "teacher");
        login.setMustChangePassword(true); // ✅ force change on first login
        loginRepo.save(login);

        // 2) Save teacher profile
        Teacher teacher = new Teacher();
        teacher.setTeacher_id(teacherId);
        teacher.setTeacher_name(req.getTeacherName());
        teacher.setTeacher_email(req.getTeacherEmail());
        teacher.setTeacher_mob(req.getTeacherMob());
        teacher.setTeacher_add(req.getTeacherAdd());
        teacher.setTeacher_exp(req.getTeacherExp());
        teacher.setTeacher_class(req.getTeacherClass());
        teacher.setImageUrl(req.getImageUrl()); // if field exists in request
        teacher.setTeacher_qualification(req.getTeacherQualification());
        teacher.setIsAdmin(isAdmin ? 1 : 0);
        teacherRepo.save(teacher);

        return teacherId;
    }
    
    @Transactional
    public void deleteTeacherById(String teacherId) {
        // first delete teacher profile (FK safe if you ever add relations)
        if (teacherRepo.existsById(teacherId)) {
            teacherRepo.deleteById(teacherId);
        }

        // then delete login entry
        if (loginRepo.existsById(teacherId)) {
            loginRepo.deleteById(teacherId);
        }
    }

}
