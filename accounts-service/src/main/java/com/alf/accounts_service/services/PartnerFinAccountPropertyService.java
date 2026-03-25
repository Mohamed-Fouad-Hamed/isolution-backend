package com.alf.accounts_service.services;

import com.alf.accounts_service.models.Account;
import com.alf.accounts_service.models.coa.FinancialAccount;
import com.alf.accounts_service.models.core.Partner;
import com.alf.accounts_service.models.core.PartnerFinAccountProperty;
import com.alf.accounts_service.models.enums.FinAccountPropertyType;
import com.alf.accounts_service.repositories.PartnerFinAccountPropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PartnerFinAccountPropertyService {
    private final PartnerFinAccountPropertyRepository repo;


    @Transactional(readOnly = true)
    public FinancialAccount getFinAccount(
            Partner partner,
            Account company,
            FinAccountPropertyType type) {

        return repo
                .findByPartnerAndAccountAndFinAccountPropertyType(
                        partner, company, type
                )
                .map(PartnerFinAccountProperty::getFinAccount)
                .orElseGet(() ->
                        getDefaultFromCompany(company, type)
                );
    }

    private FinancialAccount getDefaultFromCompany(
            Account company,
            FinAccountPropertyType type) {

        if (type == FinAccountPropertyType.RECEIVABLE) {
            return company.getDefaultReceivableAccount();
        }
        return company.getDefaultPayableAccount();
    }

    public void setFinAccount(
            Partner partner,
            Account company,
            FinAccountPropertyType type,
            FinancialAccount finAccount) {

        PartnerFinAccountProperty prop =
                repo.findByPartnerAndAccountAndFinAccountPropertyType(
                        partner, company, type
                ).orElseGet(() -> {
                    PartnerFinAccountProperty p =
                            new PartnerFinAccountProperty();
                    p.setPartner(partner);
                    p.setAccount(company);
                    p.setFinAccountPropertyType(type);
                    return p;
                });

        prop.setFinAccount(finAccount);
        repo.save(prop);
    }
}
