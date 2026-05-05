package com.auction.itemservice.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileUploadUtil {

    // ✅ absolute path (VERY IMPORTANT)
    private final String uploadDir = System.getProperty("user.dir") + "/uploads/";

    public String saveFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            // ✅ remove spaces (safe filename)
            String originalName = file.getOriginalFilename().replaceAll("\\s+", "_");

            String fileName = UUID.randomUUID() + "_" + originalName;
            String filePath = uploadDir + fileName;

            file.transferTo(new File(filePath));

            return "uploads/" + fileName; // ✅ return relative path for frontend

        } catch (IOException e) {
            e.printStackTrace(); // 🔥 VERY IMPORTANT
            throw new RuntimeException("File upload failed");
        }
    }
}