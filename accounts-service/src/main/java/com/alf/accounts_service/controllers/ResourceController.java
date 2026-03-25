package com.alf.accounts_service.controllers;


import com.alf.accounts_service.models.ResourceFile;
import com.alf.accounts_service.models.enums.ResourceType;
import com.alf.accounts_service.models.enums.ResourceUsage;
import com.alf.accounts_service.services.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@RestController
@RequestMapping("/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;


    @GetMapping("/get-resource")
    public ResponseEntity<ResourceFile> getResource(
            @RequestParam Long entityId,
            @RequestParam String entity,
            @RequestParam ResourceType resourceType,
            @RequestParam ResourceUsage usageType) {

        ResourceFile resource = resourceService.getResource(entityId, resourceType, usageType,entity);

        return ResponseEntity.ok(resource);
    }

    @PostMapping("/update-resource")
    public ResponseEntity<ResourceFile> AddOrUpdate(
            @RequestParam MultipartFile file,
            @RequestParam String entity,
            @RequestParam Long entityId,
            @RequestParam ResourceType type,
            @RequestParam ResourceUsage usage) {

        ResourceFile saved = resourceService.upload(file, entity, entityId, type, usage);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> download(@PathVariable Long id) throws FileNotFoundException {

        ResourceFile resourceFile = resourceService.getFile(id);
        Path path = Paths.get(resourceFile.getPath());

        File file = path.toFile();
        if (!file.exists()) {
            throw new FileNotFoundException("File not found at: " + file.getAbsolutePath());
        }
        Resource resource = new FileSystemResource(file);


        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(resourceFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resourceFile.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/list")
    public List<ResourceFile> listFiles(
            @RequestParam String entity,
            @RequestParam Long entityId,
            @RequestParam ResourceUsage usage) {
        return resourceService.listFiles(entity, entityId, usage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        resourceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
