package com.alf.inventory.repository;

import com.alf.inventory.entity.StockQuant;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

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

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT q FROM StockQuant q
        WHERE q.productId = :productId
        AND q.companyId = :companyId
        AND q.locationId = :locationId
        AND q.quantity > q.reservedQuantity
        ORDER BY q.inDate ASC
    """)
    List<StockQuant> findAvailableForUpdate(
            Long productId,
            Long companyId,
            Long locationId
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT q FROM StockQuant q
        WHERE q.productId = :productId
        AND q.locationId = :locationId
        AND q.companyId = :companyId
        AND (q.lotId = :lotId OR (q.lotId IS NULL AND :lotId IS NULL))
        """)
    Optional<StockQuant> findForUpdate(
            Long productId,
            Long locationId,
            Long lotId,
            Long companyId
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT q FROM StockQuant q
        WHERE q.productId = :productId
        AND q.companyId = :companyId
        AND q.locationId IN :locationIds
        AND q.quantity > q.reservedQuantity
        ORDER BY q.inDate ASC
     """)
    List<StockQuant> findAvailableOrderedByInDate(
            Long productId,
            Long companyId,
            List<Long> locationIds
    );

    @Query("""
        SELECT q FROM StockQuant q
        WHERE q.productId = :productId
        AND q.companyId = :companyId
        AND q.locationId IN :locationIds
        AND q.quantity > q.reservedQuantity
        ORDER BY q.expiryDate ASC
        """)
    List<StockQuant> findAvailableOrderedByExpiry(
            Long productId,
            Long companyId,
            List<Long> locationIds
    );

    @Query("""
        SELECT q FROM StockQuant q, StockLocation l
        WHERE q.locationId = l.id
        AND q.productId = :productId
        AND q.companyId = :companyId
        AND q.quantity > q.reservedQuantity
        ORDER BY l.priority ASC, q.inDate ASC
        """)
    List<StockQuant> findByPriority(
            Long productId,
            Long companyId
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT q
        FROM StockQuant q
        WHERE q.productId = :productId
        AND q.companyId = :companyId
        AND q.locationId IN :locationIds
        AND q.quantity > q.reservedQuantity
        ORDER BY q.inDate ASC
        """)
    List<StockQuant> findAvailableByFifo(
            Long productId,
            Long companyId,
            List<Long> locationIds
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT q
        FROM StockQuant q, StockLocation l
        WHERE q.locationId = l.id
        AND q.productId = :productId
        AND q.companyId = :companyId
        AND q.locationId IN :locationIds
        AND q.quantity > q.reservedQuantity
        ORDER BY l.priority ASC, q.inDate ASC
        """)
    List<StockQuant> findAvailableByPriority(
            Long productId,
            Long companyId,
            List<Long> locationIds
    );



    Optional<StockQuant> findOneByKeys(
            Long productId,
            Long locationId,
            Long lotId,
            Long companyId
    );
}
