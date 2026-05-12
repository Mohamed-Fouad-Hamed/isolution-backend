package com.alf.media_service.controllers;


import com.alf.media_service.services.FileStorageService;
import com.alf.media_service.services.ResourceFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/uploads")
public class MediaDisplayController {

    private final FileStorageService fileStorageService;

    @GetMapping("/{size}/{filename:.+}")
    public ResponseEntity<Resource> getFile(
            @PathVariable String size,
            @PathVariable String filename) {
        return fileStorageService.getFile(size,filename);
    }
}
