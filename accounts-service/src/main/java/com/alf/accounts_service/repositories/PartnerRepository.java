package com.alf.accounts_service.repositories;


import com.alf.accounts_service.models.core.Partner;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface PartnerRepository extends JpaRepository<Partner,Long> {

    List<Partner> findByParentId(Long parentId);

    boolean existsBySerialId(String serialId);

    Optional<Partner> findBySerialId(String serialId);

    List<Partner> findByCustomerRankGreaterThan(int rank);

    List<Partner> findBySupplierRankGreaterThan(int rank);

    @Query("""
        SELECT p
        FROM Partner p
        WHERE (:keyword IS NULL
               OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(p.displayName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
        AND (
            :lastName IS NULL
            OR LOWER(p.name) > LOWER(:lastName)
            OR (LOWER(p.name) = LOWER(:lastName) AND p.id > :lastId)
        )
        ORDER BY LOWER(p.name) ASC, p.id ASC
        """)
            List<Partner> searchAfter(
                    @Param("keyword") String keyword,
                    @Param("lastName") String lastName,
                    @Param("lastId") Long lastId,
                    Pageable pageable
            );

}
