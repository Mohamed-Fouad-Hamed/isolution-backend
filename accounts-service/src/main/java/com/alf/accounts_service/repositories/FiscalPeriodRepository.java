package com.alf.accounts_service.repositories;



import com.alf.accounts_service.models.core.FiscalPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface FiscalPeriodRepository extends JpaRepository<FiscalPeriod, Integer> {
    List<FiscalPeriod> findByFiscalYearIdOrderByStartDateAsc(Integer fiscalYearId);
    Optional<FiscalPeriod> findByNameAndFiscalYearId(String name, Integer fiscalYearId);
    boolean existsByFiscalYearIdAndIdNotAndName(Integer fiscalYearId, Integer id, String name); // For update uniqueness
    boolean existsByFiscalYearIdAndClosedFalse(Integer fiscalYearId); // Check for open periods in a year
    boolean existsByFiscalYearId(Integer fiscalYearId); // Check if any periods exist for a year
}
