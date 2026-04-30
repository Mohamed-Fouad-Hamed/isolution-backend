package com.alf.inventory.repository;

import com.alf.inventory.entity.Picking;
import com.alf.inventory.enums.PickingState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PickingRepository extends JpaRepository<Picking, Long> {

    Optional<Picking> findByCode(String code);
    List<Picking> findByState(PickingState state);
    List<Picking> findByAssignedTo(Long assignedTo);
    List<Picking> findByWarehouseId(Long warehouseId);

    @Query("""
        SELECT p
        FROM Picking p
        WHERE p.state = 'READY'
        ORDER BY p.priority ASC, p.createdAt ASC
        """)
    List<Picking> findReadyForAssignment();

}

