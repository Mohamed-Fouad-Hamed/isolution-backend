package com.alf.inventory.repository;

import com.alf.inventory.entity.StockQuant;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StockQuantRepository
        extends JpaRepository<StockQuant, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    SELECT q FROM StockQuant q
    WHERE q.productId = :productId
    AND q.companyId = :companyId
    AND q.quantity > q.reservedQuantity
""")
    List<StockQuant> findAvailableForUpdate(Long productId, Long companyId);

}
