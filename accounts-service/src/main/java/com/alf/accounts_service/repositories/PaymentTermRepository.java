package com.alf.accounts_service.repositories;

import com.alf.accounts_service.models.PaymentTerm;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentTermRepository
        extends JpaRepository<PaymentTerm, Long> {

    Optional<PaymentTerm> findByCode(String code);

    @Query("""
        SELECT i
        FROM PaymentTerm i
        WHERE (
               :keyword IS NULL
               OR LOWER(i.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(i.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
        AND (
            :lastName IS NULL
            OR LOWER(i.name) > LOWER(:lastName)
            OR (LOWER(i.name) = LOWER(:lastName) AND i.id > :lastId)
        )
        ORDER BY LOWER(i.name) ASC, i.id ASC
        """)
    List<PaymentTerm> searchAfter(
            @Param("keyword") String keyword,
            @Param("lastName") String lastName,
            @Param("lastId") Long lastId,
            Pageable pageable
    );

}

