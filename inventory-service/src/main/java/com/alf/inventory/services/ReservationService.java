package com.alf.inventory.services;

import com.alf.inventory.dto.ReservationContext;
import com.alf.inventory.dto.ReservationRequest;
import com.alf.inventory.entity.StockMove;
import com.alf.inventory.entity.StockMoveLine;
import com.alf.inventory.entity.StockQuant;
import com.alf.inventory.entity.Warehouse;
import com.alf.inventory.enums.MoveState;
import com.alf.inventory.repository.StockMoveLineRepository;
import com.alf.inventory.repository.StockMoveRepository;
import com.alf.inventory.repository.StockQuantRepository;
import com.alf.inventory.repository.WarehouseRepository;
import com.alf.inventory.strategy.interfaces.AllocationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final StockQuantRepository quantRepo;
    private final StockMoveLineRepository moveLineRepo;
    private final StockMoveRepository moveRepo;
    private final WarehouseRepository warehouseRepo;
    private final LocationService locationService;
    private final AllocationStrategyResolver resolver;

    @Transactional
    public void reserve(Long moveId) {

        StockMove move = moveRepo.findById(moveId)
                .orElseThrow();

        BigDecimal required = move.getQuantity();

        List<StockQuant> quants =
                quantRepo.findAvailableForUpdate(
                        move.getProductId(),
                        move.getCompanyId(),
                        move.getSourceLocationId()
                );

        BigDecimal remaining = required;
        BigDecimal totalReserved = BigDecimal.ZERO;

        for (StockQuant q : quants) {

            BigDecimal available =
                    q.getQuantity().subtract(q.getReservedQuantity());

            if (available.compareTo(BigDecimal.ZERO) <= 0) continue;

            BigDecimal take = available.min(remaining);

            // 🔥 update quant
            q.setReservedQuantity(
                    q.getReservedQuantity().add(take)
            );

            // 🔥 create move line
            StockMoveLine line = new StockMoveLine();
            line.setMoveId(move.getId());
            line.setProductId(move.getProductId());
            line.setSourceLocationId(move.getSourceLocationId());
            line.setDestinationLocationId(move.getDestinationLocationId());
            line.setLotId(q.getLotId());
            line.setReserveQuantity(take);
            line.setQuantityDone(BigDecimal.ZERO); // لسه متنفذش

            moveLineRepo.save(line);

            remaining = remaining.subtract(take);
            totalReserved = totalReserved.add(take);

            if (remaining.compareTo(BigDecimal.ZERO) == 0)
                break;
        }

        // 🔥 update move
        move.setReservedQuantity(totalReserved);

        if (totalReserved.compareTo(required) == 0) {
            move.setMoveState(MoveState.ASSIGNED); // fully reserved
        } else if (totalReserved.compareTo(BigDecimal.ZERO) > 0) {
            move.setMoveState(MoveState.PARTIALLY_ASSIGNED);
        } else {
            move.setMoveState(MoveState.CONFIRMED);
        }

        moveRepo.save(move);
    }

    @Transactional
    public void reserve(ReservationRequest request) {

        StockMove move = moveRepo.findById(request.moveId())
                .orElseThrow();

        Warehouse warehouse = warehouseRepo.findById(request.warehouseId())
                .orElseThrow();

        List<Long> eligibleLocations =
                locationService.findReservableLocations(warehouse.getId());

        AllocationStrategy strategy =
                resolver.resolve(warehouse.getAllocationMethod());

        ReservationContext context =
                new ReservationContext(
                        move.getProductId(),
                        move.getCompanyId(),
                        eligibleLocations,
                        move.getQuantity()
                );

        List<StockQuant> quants = strategy.allocate(context);

        BigDecimal totalAvailable = quants.stream()
                .map(q -> q.getQuantity().subtract(q.getReservedQuantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (!warehouse.getAllowPartialReservation()
                && totalAvailable.compareTo(move.getQuantity()) < 0) {

            throw new RuntimeException("Insufficient stock");
        }

        allocate(move, quants);
    }

    private void allocate(StockMove move, List<StockQuant> quants) {
        // allocation logic
        BigDecimal required = move.getQuantity();
        BigDecimal remaining = required;
        BigDecimal totalReserved = BigDecimal.ZERO;

        for (StockQuant q : quants) {

            BigDecimal available =
                    q.getQuantity().subtract(q.getReservedQuantity());

            if (available.compareTo(BigDecimal.ZERO) <= 0) continue;

            BigDecimal take = available.min(remaining);

            // 🔥 update quant
            q.setReservedQuantity(
                    q.getReservedQuantity().add(take)
            );

            // 🔥 create move line
            StockMoveLine line = new StockMoveLine();
            line.setMoveId(move.getId());
            line.setProductId(move.getProductId());
            line.setSourceLocationId(move.getSourceLocationId());
            line.setDestinationLocationId(move.getDestinationLocationId());
            line.setLotId(q.getLotId());
            line.setReserveQuantity(take);
            line.setQuantityDone(BigDecimal.ZERO); // لسه متنفذش

            moveLineRepo.save(line);

            remaining = remaining.subtract(take);
            totalReserved = totalReserved.add(take);

            if (remaining.compareTo(BigDecimal.ZERO) == 0)
                break;
        }

        // 🔥 update move
        move.setReservedQuantity(totalReserved);

        if (totalReserved.compareTo(required) == 0) {
            move.setMoveState(MoveState.ASSIGNED); // fully reserved
        } else if (totalReserved.compareTo(BigDecimal.ZERO) > 0) {
            move.setMoveState(MoveState.PARTIALLY_ASSIGNED);
        } else {
            move.setMoveState(MoveState.CONFIRMED);
        }

        moveRepo.save(move);
    }

    @Transactional
    public void unReserve(Long moveId) {

        StockMove move = moveRepo.findById(moveId).orElseThrow();

        List<StockMoveLine> lines = moveLineRepo.findByMoveId(moveId);

        BigDecimal totalUnreserved = BigDecimal.ZERO;

        for (StockMoveLine line : lines) {

            BigDecimal canUnreserve =
                    line.getReserveQuantity().subtract(line.getQuantityDone());

            if (canUnreserve.compareTo(BigDecimal.ZERO) <= 0)
                continue;

            StockQuant quant = quantRepo.findForUpdate(
                    line.getProductId(),
                    line.getSourceLocationId(),
                    line.getLotId(),
                    move.getCompanyId()
            ).orElseThrow();

            // unReserve
            quant.setReservedQuantity(
                    quant.getReservedQuantity().subtract(canUnreserve)
            );

            // edit move line
            line.setReserveQuantity(line.getQuantityDone());

            totalUnreserved = totalUnreserved.add(canUnreserve);
        }

        // update move
        move.setReservedQuantity(
                move.getReservedQuantity().subtract(totalUnreserved)
        );

        //  update state
        if (move.getReservedQuantity().compareTo(BigDecimal.ZERO) == 0) {
            move.setMoveState(MoveState.CONFIRMED);
        } else {
            move.setMoveState(MoveState.PARTIALLY_ASSIGNED);
        }
    }

    @Transactional
    public void unReservePartial(Long moveLineId, BigDecimal qtyToUnReserve) {

        StockMoveLine line = moveLineRepo.findById(moveLineId).orElseThrow();

        BigDecimal availableToUnReserve =
                line.getReserveQuantity().subtract(line.getQuantityDone());

        if (qtyToUnReserve.compareTo(availableToUnReserve) > 0) {
            throw new RuntimeException("Exceeds allowed unReserve");
        }

        StockQuant quant = quantRepo.findForUpdate(
                line.getProductId(),
                line.getSourceLocationId(),
                line.getLotId(),
                line.getCompanyId()
        ).orElseThrow();

        // 🔻 quant
        quant.setReservedQuantity(
                quant.getReservedQuantity().subtract(qtyToUnReserve)
        );

        // 🔻 move line
        line.setReserveQuantity(
                line.getReserveQuantity().subtract(qtyToUnReserve)
        );

        // 🔻 move
        StockMove move = moveRepo.findById(line.getMoveId()).orElseThrow();

        move.setReservedQuantity(
                move.getReservedQuantity().subtract(qtyToUnReserve)
        );


    }


}

