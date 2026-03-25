package com.alf.accounts_service.repositories;

import com.alf.accounts_service.models.coa.FinancialAccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FinancialAccountTypeRepository extends JpaRepository<FinancialAccountType,Integer> {

}
