package com.alf.inventory.dto;

public record CreateProductCommand(

        String sku,

        String barcode,

        Long templateId,
        Long uomId

) {}

