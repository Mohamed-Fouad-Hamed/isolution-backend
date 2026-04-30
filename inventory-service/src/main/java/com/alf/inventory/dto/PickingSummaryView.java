package com.alf.inventory.dto;

import com.alf.inventory.enums.PickingState;

public record PickingSummaryView(
        Long id,
        String code,
        PickingState state,
        Long assignedTo,
        Integer priority
) {

}
