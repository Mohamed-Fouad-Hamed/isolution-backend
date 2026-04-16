package com.alf.inventory.services;

import com.alf.inventory.entity.Lot;
import com.alf.inventory.entity.StockMove;
import com.alf.inventory.entity.StockMoveLine;
import com.alf.inventory.entity.StockQuant;
import com.alf.inventory.enums.MoveState;
import com.alf.inventory.repository.LotRepository;
import com.alf.inventory.repository.StockQuantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockQuantService {

    private final StockQuantRepository stockQuantRepository;
    private final LotRepository lotRepository;

    @Transactional
    public List<StockMoveLine> reserve(StockMove move) {

        List<StockQuant> quants = stockQuantRepository
                .findAvailableForUpdate(move.getProductId(), move.getCompanyId());

        // FEFO
        quants.sort(Comparator.comparing(q ->
                {
                    Lot lot = lotRepository.getReferenceById(q.getLotId());
                    return q.getLotId() != null ? lot.getExpiryDate() : LocalDate.MAX ;
                }
        ));

        BigDecimal remaining = move.getQuantity();
        List<StockMoveLine> lines = new ArrayList<>();

        for (StockQuant q : quants) {

            if (remaining.compareTo(BigDecimal.ZERO) <= 0) break;

            BigDecimal available = q.getAvailable();
            if (available.compareTo(BigDecimal.ZERO) <= 0) continue;

            BigDecimal take = available.min(remaining);

            // update reservation
            q.setReservedQuantity(q.getReservedQuantity().add(take));

            StockMoveLine line = new StockMoveLine();
            line.setMoveId(move.getId());
            line.setLotId(q.getLotId());
            line.setProductId(q.getProductId());
            line.setSourceLocationId(q.getLocationId());
            line.setDestinationLocationId(move.getDestinationLocationId());
            line.setQtyDone(take);

            lines.add(line);

            remaining = remaining.subtract(take);
        }

        move.setState(remaining.compareTo(BigDecimal.ZERO) == 0
                ? MoveState.ASSIGNED
                : MoveState.CONFIRMED // partial
        );

        return lines;
    }


    @Transactional
    public void adjust(StockQuant quant, BigDecimal actualQty) {

        BigDecimal diff = actualQty.subtract(quant.getQuantity());

        quant.setQuantity(actualQty);

        if (quant.getReservedQuantity().compareTo(actualQty) > 0) {
            quant.setReservedQuantity(actualQty);
        }

        stockQuantRepository.save(quant);

        // optional: create audit move
    }


}
