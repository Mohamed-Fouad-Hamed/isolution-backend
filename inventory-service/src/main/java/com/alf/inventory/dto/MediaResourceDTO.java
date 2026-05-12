package com.alf.inventory.dto;

public record MediaResourceDTO(
        String url,
        String thumbnailUrl,
        String resourceType,
        Long resourceId
) {
}
