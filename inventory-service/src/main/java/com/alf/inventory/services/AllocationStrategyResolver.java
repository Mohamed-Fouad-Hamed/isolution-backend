package com.alf.inventory.services;

import com.alf.inventory.enums.AllocationMethod;
import com.alf.inventory.strategy.implement.FefoAllocationStrategy;
import com.alf.inventory.strategy.implement.FifoAllocationStrategy;
import com.alf.inventory.strategy.implement.PriorityAllocationStrategy;
import com.alf.inventory.strategy.interfaces.AllocationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AllocationStrategyResolver {

    private final FifoAllocationStrategy fifo;
    private final FefoAllocationStrategy fefo;
    private final PriorityAllocationStrategy priority;

    public AllocationStrategy resolve(AllocationMethod method) {
        return switch (method) {
            case FIFO -> fifo;
            case FEFO -> fefo;
            case LOCATION_PRIORITY -> priority;
        };
    }
}

