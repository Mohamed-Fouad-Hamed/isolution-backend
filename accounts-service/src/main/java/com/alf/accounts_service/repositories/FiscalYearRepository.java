package com.alf.accounts_service.repositories;


import com.alf.accounts_service.models.core.FiscalYear;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface FiscalYearRepository extends JpaRepository<FiscalYear, Integer> {
    List<FiscalYear> findByAccountIdOrderByNameAsc(Long accountId);

    Optional<FiscalYear> findByNameAndAccountId(String name, Long accountId);


    Optional<FiscalYear> findBySerialId(String serialId );
    boolean existsByAccountIdAndIdNotAndName(Long accountId, Integer id, String name); // For update uniqueness

}
