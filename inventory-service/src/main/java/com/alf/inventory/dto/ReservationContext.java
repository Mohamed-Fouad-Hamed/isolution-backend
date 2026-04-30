package com.alf.inventory.dto;

import java.math.BigDecimal;
import java.util.List;

public record ReservationContext(
        Long productId,
        Long companyId,
        List<Long> locationIds,
        BigDecimal requestedQty
) {
}
