package com.alf.inventory.repository;

import com.alf.inventory.dto.PickingSummaryView;
import com.alf.inventory.entity.PickingLine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PickingLineRepository extends JpaRepository<PickingLine, Long> {
    List<PickingLine> findByPickingId(Long pickingId);
    Optional<PickingLine> findByMoveLineId(Long moveLineId);
    List<PickingLine> findBySourceLocationId(Long sourceLocationId);

    @Query("""
        SELECT pl
        FROM PickingLine pl
        WHERE pl.pickingId = :pickingId
        AND pl.state <> 'PICKED'
        """)
    List<PickingLine> findOpenLines(Long pickingId);

    @Query("""
    SELECT new com.alf.inventory.dto.PickingSummaryView(
        p.id,
        p.code,
        p.state,
        p.assignedTo,
        p.priority
    )
    FROM Picking p
    """)
    Page<PickingSummaryView> findAllSummary(Pageable pageable);

}

