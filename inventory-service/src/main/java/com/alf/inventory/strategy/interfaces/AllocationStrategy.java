package com.alf.inventory.strategy.interfaces;

import com.alf.inventory.dto.ReservationContext;
import com.alf.inventory.entity.StockQuant;

import java.util.List;

public interface AllocationStrategy {
    List<StockQuant> allocate(ReservationContext context);
}
