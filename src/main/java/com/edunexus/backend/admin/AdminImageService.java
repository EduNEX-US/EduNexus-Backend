package com.edunexus.backend.admin;

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
public class AdminImageService {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    public String saveAdminImage(MultipartFile file) throws Exception {
        String original = StringUtils.cleanPath(file.getOriginalFilename());
        String ext = "";
        int dot = original.lastIndexOf('.');
        if (dot >= 0) ext = original.substring(dot).toLowerCase();

        if (!ext.matches("\\.(png|jpg|jpeg|webp)$")) {
            throw new RuntimeException("Only png/jpg/jpeg/webp allowed");
        }

        String filename = UUID.randomUUID() + ext;

        Path dirPath = Paths.get(uploadDir, "admins").toAbsolutePath().normalize();
        Files.createDirectories(dirPath);

        Path target = dirPath.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/admins/" + filename;
    }

    public void deleteAdminImageByUrl(String imageUrl) throws Exception {
        // imageUrl: "/uploads/admins/abc.png"
        if (imageUrl == null || imageUrl.isBlank()) return;

        String filename = imageUrl.replaceFirst("^/uploads/admins/", "");
        Path filePath = Paths.get(uploadDir, "admins").toAbsolutePath().normalize().resolve(filename);
        Files.deleteIfExists(filePath);
    }
}
