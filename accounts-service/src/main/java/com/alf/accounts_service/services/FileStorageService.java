package com.alf.accounts_service.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    @Value("${app.file.upload-dir}")
    private String rootPath;

    private final String ResourceUploads = "/uploads/files/";

    private Path root;

    @PostConstruct
    public void init() {
        this.root = Paths.get(rootPath); // دلوقتي rootPath موجود
    }

    public String save(MultipartFile file, String folder) {
        try {
            Path folderPath = root.resolve(folder);
            Files.createDirectories(folderPath);

            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path destination = folderPath.resolve(filename);
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            return ResourceUploads + folder + "/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("File save failed", e);
        }
    }

    public void delete(String relativePath) {
        try {
            Files.deleteIfExists(root.resolve(relativePath));
        } catch (IOException ignored) {}
    }
}
