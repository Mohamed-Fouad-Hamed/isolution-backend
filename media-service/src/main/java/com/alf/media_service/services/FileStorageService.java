package com.alf.media_service.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    @Value("${app.file.upload-dir}")
    private String rootPath;

    private Path root;

    private  Path thumbRoot;

    @PostConstruct
    public void init() {

        root = Paths.get(rootPath);
        thumbRoot = Paths.get(rootPath + "/thumbnails");
    }

    public Map<String,String> save(MultipartFile file) {
        try {
            String newFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), this.root.resolve(newFileName));

            // 2. إنشاء وتصغير الصورة (مثلاً 200x200) وحفظها
            String thumbName = "thumb_" + newFileName;
            Thumbnails.of(file.getInputStream())
                    .size(200, 200)
                    .outputFormat("jpg") // توحيد الامتداد للنسخ المصغرة
                    .toFile(this.thumbRoot.resolve(thumbName).toFile());

            // إرجاع الروابط
            return Map.of(
                    "original", "/uploads/original/" + newFileName,
                    "thumbnail", "/uploads/thumb/" + thumbName
            );

        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }


    public ResponseEntity<Resource> getFile(
             String size,
             String filename) {
        try {

            Path basePath = "thumb".equalsIgnoreCase(size) ? thumbRoot : root;
            Path file = basePath.resolve(filename).normalize();


            if (!file.startsWith(basePath)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                String contentType = Files.probeContentType(file);

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, contentType != null ? contentType : "application/octet-stream")
                        .header(HttpHeaders.CACHE_CONTROL, "public, max-age=31536000")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public void delete(String relativePath) {
        try {
            Files.deleteIfExists(root.resolve(relativePath));
        } catch (IOException ignored) {}
    }
}
