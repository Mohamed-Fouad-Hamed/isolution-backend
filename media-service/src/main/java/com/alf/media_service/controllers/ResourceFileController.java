package com.alf.media_service.controllers;

import com.alf.media_service.DTO.ResourceFileResponse;
import com.alf.media_service.entities.ResourceFile;
import com.alf.media_service.services.ResourceFileService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resources")
public class ResourceFileController {

    private final ResourceFileService resourceFileService;

    @PostMapping("/upload")
    public ResponseEntity<ResourceFileResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        ResourceFile resourceFile = resourceFileService.uploadResource(file);

        return ResponseEntity.ok(new ResourceFileResponse(
                resourceFile.getId(),
                resourceFile.getFileUrl(),
                resourceFile.getThumbnailUrl(),
                resourceFile.getFileName(),
                resourceFile.getContentType(),
                resourceFile.getFileSize()
        ));
    }


}
