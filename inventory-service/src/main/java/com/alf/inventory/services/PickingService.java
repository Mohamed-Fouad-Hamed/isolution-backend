package com.alf.inventory.services;

import com.alf.inventory.dto.PickLineRequest;
import com.alf.inventory.entity.Picking;
import com.alf.inventory.entity.PickingLine;
import com.alf.inventory.entity.StockMove;
import com.alf.inventory.entity.StockMoveLine;
import com.alf.inventory.enums.OperationType;
import com.alf.inventory.enums.PickingLineState;
import com.alf.inventory.enums.PickingState;
import com.alf.inventory.repository.PickingLineRepository;
import com.alf.inventory.repository.PickingRepository;
import com.alf.inventory.repository.StockMoveLineRepository;
import com.alf.inventory.repository.StockMoveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PickingService {

    private final PickingRepository pickingRepo;
    private final PickingLineRepository lineRepo;
    private final StockMoveRepository moveRepo;
    private final StockMoveLineRepository moveLineRepo;

    @Transactional
    public Picking createFromMove(Long moveId) {

        StockMove move = moveRepo.findById(moveId)
                .orElseThrow();

        List<StockMoveLine> moveLines =
                moveLineRepo.findByMoveId(moveId);

        Picking picking = Picking.builder()
                .code(generateCode())
                .warehouseId(1L) // لاحقًا derive dynamically
                .companyId(move.getCompanyId())
                .operationType(OperationType.OUTGOING)
                .state(PickingState.READY)
                .reference("MOVE-" + moveId)
                .build();

        pickingRepo.save(picking);

        List<PickingLine> lines = moveLines.stream()
                .map(line -> PickingLine.builder()
                        .pickingId(picking.getId())
                        .moveLineId(line.getId())
                        .productId(line.getProductId())
                        .sourceLocationId(line.getSourceLocationId())
                        .destinationLocationId(line.getDestinationLocationId())
                        .lotId(line.getLotId())
                        .reservedQty(line.getReserveQuantity())
                        .pickedQty(BigDecimal.ZERO)
                        .state(PickingLineState.WAITING)
                        .build()
                ).toList();

        lineRepo.saveAll(lines);

        return picking;
    }

    @Transactional
    public void assignPicker(Long pickingId, Long userId) {

        Picking picking = pickingRepo.findById(pickingId)
                .orElseThrow();

        picking.setAssignedTo(userId);
    }

    @Transactional
    public void startPicking(Long pickingId) {

        Picking picking = pickingRepo.findById(pickingId)
                .orElseThrow();

        if (picking.getState() != PickingState.READY) {
            throw new RuntimeException("Picking not ready");
        }

        picking.setState(PickingState.IN_PROGRESS);
        picking.setStartedAt(LocalDateTime.now());
    }

    @Transactional
    public void pickLine(Long pickingId, PickLineRequest request) {

        PickingLine line = lineRepo.findById(request.lineId())
                .orElseThrow();

        if (!line.getPickingId().equals(pickingId)) {
            throw new RuntimeException("Line does not belong to picking");
        }

        BigDecimal total =
                line.getPickedQty().add(request.pickedQty());

        if (total.compareTo(line.getReservedQty()) > 0) {
            throw new RuntimeException("Picked exceeds reserved");
        }

        line.setPickedQty(total);
        line.setScannedBarcode(request.barcode());
        line.setPickedAt(LocalDateTime.now());

        if (total.compareTo(line.getReservedQty()) == 0) {
            line.setState(PickingLineState.PICKED);
        } else {
            line.setState(PickingLineState.PARTIAL);
        }
    }

    @Transactional
    public void completePicking(Long pickingId) {

        Picking picking = pickingRepo.findById(pickingId)
                .orElseThrow();

        List<PickingLine> lines =
                lineRepo.findByPickingId(pickingId);

        boolean allDone = lines.stream()
                .allMatch(line ->
                        line.getPickedQty()
                                .compareTo(line.getReservedQty()) == 0
                );

        if (!allDone) {
            throw new RuntimeException("Not all lines fully picked");
        }

        // هنا لاحقًا تنادى completeMove()
        picking.setState(PickingState.DONE);
        picking.setCompletedAt(LocalDateTime.now());
    }

    @Transactional
    public void cancelPicking(Long pickingId) {

        Picking picking = pickingRepo.findById(pickingId)
                .orElseThrow();

        picking.setState(PickingState.CANCELLED);
    }




    private String generateCode() {
        return "PK-" + System.currentTimeMillis();
    }


}
