package com.mobile.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileStorageService {
    @Value("${app.storage.dir}")
    private String storageDir;
    @Value("${app.public.base-url:http://localhost:8080}")
    private String baseUrl;

    public String store(MultipartFile file, String prefix) throws IOException {
        String ext = Optional.ofNullable(file.getOriginalFilename())
                .filter(f -> f.contains("."))
                .map(f -> f.substring(f.lastIndexOf('.')))
                .orElse(".jpg");
        String filename = prefix + "_" + UUID.randomUUID() + ext;

        Path dir = Paths.get(storageDir).toAbsolutePath().normalize();
        Files.createDirectories(dir);

        Path target = dir.resolve(filename).normalize();
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }
        return baseUrl + "/photos/" + filename;
    }

    public boolean deleteIfExists(String publicUrl) throws IOException {
        if (publicUrl == null || !publicUrl.startsWith("/photos/")) return false;
        Path dir = Paths.get(storageDir).toAbsolutePath().normalize();
        Path target = dir.resolve(publicUrl.replace("/photos/", "")).normalize();
        return Files.deleteIfExists(target);
    }
}

