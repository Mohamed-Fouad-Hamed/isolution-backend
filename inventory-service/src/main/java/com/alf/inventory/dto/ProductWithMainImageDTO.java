package com.alf.inventory.dto;

public record ProductWithMainImageDTO(
        Long productId,
        String productName,
        Long resourceId,
        String resourceUrl,
        String thumbnailUrl
) {}