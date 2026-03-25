package com.alf.accounts_service.repositories;


import com.alf.accounts_service.models.Industry;
import com.alf.accounts_service.models.core.Partner;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IndustryRepository extends JpaRepository<Industry, Long> {

    Optional<Industry> findByCode(String code);

    List<Industry> findByParentIsNull();

    boolean existsByCode(String code);

    @Query("""
        SELECT i
        FROM Industry i
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
    List<Industry> searchAfter(
            @Param("keyword") String keyword,
            @Param("lastName") String lastName,
            @Param("lastId") Long lastId,
            Pageable pageable
    );

}