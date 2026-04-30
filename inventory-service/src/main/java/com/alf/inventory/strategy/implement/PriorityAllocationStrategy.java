package com.alf.inventory.strategy.implement;

import com.alf.inventory.dto.ReservationContext;
import com.alf.inventory.entity.StockQuant;
import com.alf.inventory.repository.StockQuantRepository;
import com.alf.inventory.strategy.interfaces.AllocationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PriorityAllocationStrategy implements AllocationStrategy {

    private final StockQuantRepository repo;

    @Override
    public List<StockQuant> allocate(ReservationContext context) {

        return repo.findAvailableByPriority(
                context.productId(),
                context.companyId(),
                context.locationIds()
        );
    }
}

