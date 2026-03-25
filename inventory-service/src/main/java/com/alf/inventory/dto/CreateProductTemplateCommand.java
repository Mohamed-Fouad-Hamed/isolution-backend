package com.alf.inventory.dto;

import com.alf.inventory.enums.ProductType;

import java.math.BigDecimal;

public record CreateProductTemplateCommand(

        String name,

        ProductType productType,

        Long categoryId,

        Long uomId,

        Long purchaseUomId,

        BigDecimal weight,

        BigDecimal volume

) {}
