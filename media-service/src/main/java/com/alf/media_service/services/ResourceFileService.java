package com.alf.media_service.services;


import com.alf.media_service.entities.ResourceFile;
import com.alf.media_service.repositories.ResourceFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResourceFileService {

    private final ResourceFileRepository resourceFileRepository;
    private final FileStorageService fileStorageService;

    public ResourceFile uploadResource( MultipartFile file){

        Map<String,String> fileUrls = fileStorageService.save(file);

        ResourceFile resource = new ResourceFile();
        resource.setFileName(file.getOriginalFilename());
        resource.setFileUrl(fileUrls.get("original"));
        resource.setThumbnailUrl(fileUrls.get("thumbnail"));
        resource.setContentType(file.getContentType());
        resource.setFileSize(file.getSize());

        return resourceFileRepository.save(resource);

    }

}
