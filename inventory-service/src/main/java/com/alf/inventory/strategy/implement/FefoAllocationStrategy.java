package com.alf.inventory.strategy.implement;

import com.alf.inventory.dto.ReservationContext;
import com.alf.inventory.entity.StockQuant;
import com.alf.inventory.repository.StockQuantRepository;
import com.alf.inventory.strategy.interfaces.AllocationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FefoAllocationStrategy implements AllocationStrategy {

    private final StockQuantRepository repo;

    @Override
    public List<StockQuant> allocate(ReservationContext context) {

        return repo.findAvailableOrderedByExpiry(
                context.productId(),
                context.companyId(),
                context.locationIds()
        );
    }
}

