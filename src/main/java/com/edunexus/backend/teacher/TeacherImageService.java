package com.edunexus.backend.teacher;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class TeacherImageService {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    public String saveTeacherImage(MultipartFile file) throws Exception {
        String original = StringUtils.cleanPath(file.getOriginalFilename());
        String ext = "";
        int dot = original.lastIndexOf('.');
        if (dot >= 0) ext = original.substring(dot).toLowerCase();

        if (!ext.matches("\\.(png|jpg|jpeg|webp)$")) {
            throw new RuntimeException("Only png/jpg/jpeg/webp allowed");
        }

        String filename = UUID.randomUUID().toString() + ext;

        Path dirPath = Paths.get(uploadDir).toAbsolutePath().normalize().resolve("teachers");
        Files.createDirectories(dirPath);

        Path target = dirPath.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/teachers/" + filename;
    }

    public void deleteTeacherImageByUrl(String imageUrl) throws Exception {
        if (imageUrl == null || imageUrl.isBlank()) return;

        String filename = imageUrl.replaceFirst("^/uploads/teachers/", "");
        Path filePath = Paths.get(uploadDir).toAbsolutePath().normalize()
                .resolve("teachers")
                .resolve(filename);

        Files.deleteIfExists(filePath);
    }
}
