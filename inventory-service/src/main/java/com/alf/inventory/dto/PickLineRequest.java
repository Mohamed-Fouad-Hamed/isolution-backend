package com.alf.inventory.dto;

import java.math.BigDecimal;

public record PickLineRequest(
        Long lineId,
        BigDecimal pickedQty,
        String barcode
) {
}


