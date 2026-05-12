package com.alf.inventory.services;

import com.alf.inventory.entity.StockMove;
import com.alf.inventory.entity.StockMoveLine;
import com.alf.inventory.entity.StockQuant;
import com.alf.inventory.enums.MoveState;
import com.alf.inventory.repository.StockMoveLineRepository;
import com.alf.inventory.repository.StockMoveRepository;
import com.alf.inventory.repository.StockQuantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockMoveService {

    private final StockMoveRepository moveRepo;
    private final StockMoveLineRepository moveLineRepo;
    private final StockQuantRepository quantRepo;

    public StockMove create(StockMove move){
        return moveRepo.save(move);
    }


    @Transactional
    public void completeMove(Long moveId) {

        StockMove move = moveRepo.findById(moveId).orElseThrow();

        List<StockMoveLine> lines = moveLineRepo.findByMoveId(moveId);

        lines.forEach(l -> {
            if (l.getQuantityDone() == null) {
                l.setQuantityDone(l.getReserveQuantity());
            }
        });

        for (StockMoveLine line : lines) {

            BigDecimal qty = line.getQuantityDone();

            if (qty.compareTo(BigDecimal.ZERO) <= 0) continue;

            // 🔻 source
            StockQuant source = findQuantOrThrow(
                    line.getProductId(),
                    line.getSourceLocationId(),
                    line.getLotId(),
                    move.getCompanyId()
            );

            source.setQuantity(source.getQuantity().subtract(qty));
            source.setReservedQuantity(
                    source.getReservedQuantity().subtract(qty)
            );

            // 🔺 destination
            StockQuant dest = findOrCreateQuant(
                    line.getProductId(),
                    line.getDestinationLocationId(),
                    line.getLotId(),
                    move.getCompanyId()
            );

            dest.setQuantity(dest.getQuantity().add(qty));
        }

        move.setMoveState(MoveState.DONE);
    }

    private StockQuant findQuantOrThrow(
            Long productId,
            Long locationId,
            Long lotId,
            Long companyId) {

        return quantRepo.findOneByKeys(productId, locationId, lotId, companyId)
                .orElseThrow(() -> new RuntimeException("Quant not found"));
    }

    private StockQuant findOrCreateQuant(
            Long productId,
            Long locationId,
            Long lotId,
            Long companyId) {

        return quantRepo.findOneByKeys(productId, locationId, lotId, companyId)
                .orElseGet(() -> {

                    StockQuant q = new StockQuant();
                    q.setProductId(productId);
                    q.setLocationId(locationId);
                    q.setLotId(lotId);
                    q.setCompanyId(companyId);
                    q.setQuantity(BigDecimal.ZERO);
                    q.setReservedQuantity(BigDecimal.ZERO);

                    return quantRepo.save(q);
                });
    }




}
