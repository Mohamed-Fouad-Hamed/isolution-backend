package com.alf.inventory.dto;

public record ReservationRequest(
        Long moveId,
        Long warehouseId
) {
}
