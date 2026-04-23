package com.alf.inventory.dto;

public record UpdateProductCommand(

        String sku,
        String barcode,
        Long templateId

) {}