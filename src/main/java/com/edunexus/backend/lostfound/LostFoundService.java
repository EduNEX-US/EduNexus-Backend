package com.edunexus.backend.lostfound;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LostFoundService {

    @Autowired
    private LostFoundRepository lostFoundRepo;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir; // ✅ ADD THIS

    
    public LostFound addLostItem(LostFoundRequest req) {
        String uniqueItemId = "ITM" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();

        LostFound lostfound = new LostFound();
        lostfound.setItemId(uniqueItemId);
        lostfound.setItemName(req.getItemName());
        lostfound.setItemDescription(req.getItemDescription());
        lostfound.setDate(req.getItemDate());
        lostfound.setAssignedTo(req.getAssignedTo());
        lostfound.setDelivered(false);
        return lostFoundRepo.save(lostfound);
    }

    public List<LostFound> getAllItems() {
        return lostFoundRepo.findAll();
    }

    public String saveImage(MultipartFile file) throws Exception {
        String original = StringUtils.cleanPath(file.getOriginalFilename());
        String ext = "";
        int dot = original.lastIndexOf('.');
        if (dot >= 0) ext = original.substring(dot).toLowerCase();

        if (!ext.matches("\\.(png|jpg|jpeg|webp)$")) {
            throw new RuntimeException("Only png/jpg/jpeg/webp allowed");
        }

        String filename = UUID.randomUUID().toString() + ext;

        Path dirPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(dirPath);

        Path target = dirPath.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/" + filename;
    }
    
    public LostFound addLostItemMultipart(String name, String description, String assignedTo, String date, String imageUrl) {
        String uniqueItemId = "ITM" + UUID.randomUUID().toString().substring(0,5).toUpperCase();

        LostFound item = new LostFound();
        item.setItemId(uniqueItemId); // ✅ REQUIRED
        item.setItemName(name);
        item.setItemDescription(description);
        item.setAssignedTo(assignedTo);
        item.setDate(date);
        item.setDelivered(false);
        item.setImageUrl(imageUrl);

        return lostFoundRepo.save(item);
    }
    
    public void deleteItemById(String itemId) throws Exception {
        LostFound item = lostFoundRepo.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found: " + itemId));

        // delete image file from disk if present
        String imageUrl = item.getImageUrl();
        if (imageUrl != null && !imageUrl.isBlank()) {
            // imageUrl is like "/uploads/abc.jpg"
            String filename = imageUrl.replaceFirst("^/uploads/", "");
            Path filePath = Paths.get(uploadDir).toAbsolutePath().normalize().resolve(filename);

            // safety: delete only if it exists
            Files.deleteIfExists(filePath);
        }

        // delete record from DB
        lostFoundRepo.deleteById(itemId);
    }

    public LostFound updateItemMultipart(
            String itemId,
            String name,
            String description,
            String assignedTo,
            String date,
            MultipartFile image
    ) throws Exception {

        LostFound existing = lostFoundRepo.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found: " + itemId));

        // Update text fields
        existing.setItemName(name);
        existing.setItemDescription(description);
        existing.setAssignedTo(assignedTo);
        existing.setDate(date);

        // If a new image is provided -> delete old file + save new file
        if (image != null && !image.isEmpty()) {
            // delete old image file if present
            String oldUrl = existing.getImageUrl();
            if (oldUrl != null && !oldUrl.isBlank()) {
                String oldFilename = oldUrl.replaceFirst("^/uploads/", "");
                Path oldPath = Paths.get(uploadDir).toAbsolutePath().normalize().resolve(oldFilename);
                Files.deleteIfExists(oldPath);
            }

            // save new image and set url
            String newUrl = saveImage(image);
            existing.setImageUrl(newUrl);
        }

        return lostFoundRepo.save(existing);
    }


}
