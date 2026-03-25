package com.alf.inventory.dto;

import com.alf.inventory.enums.ProductType;

import java.math.BigDecimal;

public record ProductTemplateResponse(

        Long id,

        String name,

        ProductType productType,

        Long uomId,

        Long purchaseUomId,

        BigDecimal weight,

        BigDecimal volume,

        Boolean active

) {}
