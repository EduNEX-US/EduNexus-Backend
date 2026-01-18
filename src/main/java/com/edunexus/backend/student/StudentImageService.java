package com.edunexus.backend.student;

import java.nio.file.*;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StudentImageService {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    public String saveStudentImage(MultipartFile file) throws Exception {
        String original = StringUtils.cleanPath(file.getOriginalFilename());
        String ext = "";
        int dot = original.lastIndexOf('.');
        if (dot >= 0) ext = original.substring(dot).toLowerCase();

        if (!ext.matches("\\.(png|jpg|jpeg|webp)$")) {
            throw new RuntimeException("Only png/jpg/jpeg/webp allowed");
        }

        String filename = UUID.randomUUID().toString() + ext;

        Path dirPath = Paths.get(uploadDir, "students").toAbsolutePath().normalize();
        Files.createDirectories(dirPath);

        Path target = dirPath.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/students/" + filename;
    }
}
