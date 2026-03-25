package com.alf.accounts_service.services;


import com.alf.accounts_service.models.ResourceFile;
import com.alf.accounts_service.models.enums.ResourceType;
import com.alf.accounts_service.models.enums.ResourceUsage;
import com.alf.accounts_service.repositories.ResourceFileRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ResourceService {


    private final ResourceFileRepository resourceRepo;
    private final FileStorageService fileStorageService;



    /* CREATE / UPLOAD */
    @Transactional
    public ResourceFile upload(MultipartFile file, String entity, Long entityId, ResourceType type, ResourceUsage usage) {
        try {

            if(usage == ResourceUsage.PROFILE_PIC){
                resourceRepo.findByOwnerEntityAndOwnerIdAndUsageTypeAndActiveTrue(
                        entity, entityId, usage
                ).forEach(r -> r.setActive(false));
            }

            if(file.isEmpty())
                return null;

            String folder =  entity.toLowerCase() + "/" + entityId;
            String aliasPath = fileStorageService.save(file ,folder);
            ResourceFile resource = new ResourceFile();
            resource.setFilename(file.getOriginalFilename());
            resource.setFileType(file.getContentType());
            resource.setPath(aliasPath);
            resource.setSize(file.getSize());
            resource.setResourceType(type);
            resource.setUsageType(usage);
            resource.setOwnerEntity(entity);
            resource.setOwnerId(entityId);

            return resourceRepo.save(resource);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    public ResourceFile getResource( Long entityId, ResourceType resourceType, ResourceUsage resourceUsage, String ownerEntity) {
        ResourceFile resourceFile = null;
        try {
            List<ResourceFile> resourceFileList
            = resourceRepo.findByResourceTypeAndOwnerEntityAndOwnerIdAndUsageTypeAndActiveTrue
                            (resourceType,ownerEntity,entityId,resourceUsage);
            if(!resourceFileList.isEmpty())
                resourceFile = resourceFileList.get(0);
            return resourceFile;

        } catch (Exception e) {
            return  null;
            //throw new RuntimeException("Failed to store file", e);
        }

    }

   private String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return ""; // Handle null or empty filenames
        }

        int dotIndex = fileName.lastIndexOf('.'); // Find the index of the last dot

        // Check if a dot exists and is not the first character (e.g., ".bashrc")
        // and not the last character (e.g., "filename.")
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1); // Extract the substring after the dot
        } else {
            return ""; // No valid extension found
        }
    }

    public ResourceFile getFile(Long fileId) {
        return resourceRepo.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));
    }

    private String getStringExpectedValue(String fullString ,String expectedValueIndicator){
        String result = "" ;
        int startIndex = fullString.indexOf(expectedValueIndicator);

        if (startIndex != -1) { // Check if the indicator is found
            // Calculate the actual start of the desired string
            int dataStartIndex = startIndex + expectedValueIndicator.length();
            String extractedString = fullString.substring(dataStartIndex);
            result = extractedString;
        }

        return result;

    }


    public List<ResourceFile> listFiles(String entityType, Long entityId, ResourceUsage usageType) {
        return resourceRepo.findByOwnerEntityAndOwnerIdAndUsageType(entityType, entityId, usageType);
    }

    /* READ */
    public List<ResourceFile> getResources(
            String ownerEntity,
            Long ownerId,
            ResourceUsage usage
    ) {
        return usage == null
                ? resourceRepo.findByOwnerEntityAndOwnerIdAndActiveTrue(ownerEntity, ownerId)
                : resourceRepo.findByOwnerEntityAndOwnerIdAndUsageTypeAndActiveTrue(ownerEntity, ownerId, usage);
    }

    /* UPDATE (metadata only) */
    @Transactional
    public ResourceFile updateOrder(Long id, Integer order) {
        ResourceFile r = resourceRepo.findByIdAndActiveTrue(id).orElseThrow();
        r.setSortOrder(order);
        return r;
    }

    /* DELETE (soft) */
    @Transactional
    public void delete(Long id) {
        ResourceFile r = resourceRepo.findById(id).orElseThrow();
        r.setActive(false);
    }

}
