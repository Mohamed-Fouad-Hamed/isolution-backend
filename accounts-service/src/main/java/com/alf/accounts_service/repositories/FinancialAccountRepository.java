package com.alf.accounts_service.repositories;

import com.alf.accounts_service.models.coa.FinancialAccount;
import com.alf.accounts_service.models.core.Partner;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FinancialAccountRepository extends JpaRepository<FinancialAccount, Integer> {

    @Query("SELECT f FROM FinancialAccount f WHERE f.serialId = :id AND f.account.id = :accountId")
    Optional<FinancialAccount> findBySerialIdAndAccountId_IgnoreSoftDelete(@Param("id") String id , @Param("accountId") Long accountId);

    // Find by ID scoped to the parent Account
    Optional<FinancialAccount> findByIdAndAccountId(Integer id, Long accountId);

    // Find by Serial ID (assuming globally unique)
    Optional<FinancialAccount> findBySerialId(String serialId);

    Optional<FinancialAccount> findBySerialIdAndAccountId(String serialId,Long accountId);

    // Find by Serial ID scoped to the parent Account (if serialId isn't globally unique)
    // Optional<FinancialAccount> findBySerialIdAndAccountId(String serialId, Integer accountId);

    // Check existence by Serial ID (globally)
    boolean existsBySerialId(String serialId);

    // Check existence by Serial ID scoped to the parent Account
    boolean existsBySerialIdAndAccountId(String serialId, Long accountId);

    // Find all accounts for a specific parent Account
    List<FinancialAccount> findByAccountId(Long accountId);

    // Find active accounts for a specific parent Account
    List<FinancialAccount> findByAccountIdAndIsActiveTrue(Long accountId);

    // Find children of a specific parent serial ID, scoped by parent Account
    List<FinancialAccount> findByParentSerialIdAndAccountId(String parentSerialId, Long accountId);

    // Find root accounts (no parent) for a specific parent Account
    List<FinancialAccount> findByAccountIdAndParentSerialIdIsNull(Long accountId);

    @Query("""
            SELECT ac
            FROM FinancialAccount ac
            WHERE ( ac.account = :accountId ) AND ( :keyword IS NULL
                   OR LOWER(ac.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
            AND (
                :lastName IS NULL
                OR LOWER(ac.name) > LOWER(:lastName)
                OR (LOWER(ac.name) = LOWER(:lastName) AND ac.id > :lastId)
            )
            ORDER BY LOWER(ac.name) ASC, ac.id ASC
        """)
    List<FinancialAccount> searchAfter(
            @Param("accountId") Long accountId,
            @Param("keyword") String keyword,
            @Param("lastName") String lastName,
            @Param("lastId") Long lastId,
            Pageable pageable
    );
}
