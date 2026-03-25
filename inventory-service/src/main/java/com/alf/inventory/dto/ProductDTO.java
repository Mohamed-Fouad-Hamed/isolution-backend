package com.alf.inventory.dto;

public record ProductDTO(

        Long id,
        String name,
        String sku,
        Long templateId,
        Boolean active

) {}

