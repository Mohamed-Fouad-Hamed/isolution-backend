package com.alf.accounts_service.repositories;

import com.alf.accounts_service.models.Account;
import com.alf.accounts_service.models.coa.FinancialAccount;
import com.alf.accounts_service.models.core.Partner;
import com.alf.accounts_service.models.core.PartnerFinAccountProperty;
import com.alf.accounts_service.models.enums.FinAccountPropertyType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartnerFinAccountPropertyRepository extends JpaRepository<PartnerFinAccountProperty,Long> {
    Optional<PartnerFinAccountProperty> findByPartnerAndAccountAndFinAccountPropertyType(Partner partner, Account account, FinAccountPropertyType finAccountPropertyType);

}
