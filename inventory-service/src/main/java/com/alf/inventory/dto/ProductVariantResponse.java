package com.alf.inventory.dto;

public record ProductVariantResponse(
        Long id,
        String sku,
        String productName,
        String uomName,
        String barcode,
        Boolean active
) {
}
