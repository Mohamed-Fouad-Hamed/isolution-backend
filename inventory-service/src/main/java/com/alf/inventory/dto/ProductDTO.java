package com.alf.inventory.dto;

public record ProductDTO(

        Long id,
        String sku,
        Long templateId,
        Boolean active

) {}

