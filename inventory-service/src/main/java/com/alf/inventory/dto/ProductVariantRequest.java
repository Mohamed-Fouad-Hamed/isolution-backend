package com.alf.inventory.dto;

public record ProductVariantRequest(
        String sku,
        Long productTemplateId,
        String barcode,
        Boolean active
) {
}
