package com.alf.accounts_service.mappers;

import com.alf.accounts_service.dtos.account.AccountDto;
import com.alf.accounts_service.dtos.company.CompanyHierarchyDto;
import com.alf.accounts_service.models.Account;
import com.alf.accounts_service.repositories.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountMapperImpl implements  AccountMapper{

    private final PaymentRepository paymentRepository;

    @Override
    public AccountDto toAccountDto(Account account) {
        if ( account == null ) {
            return null;
        }

        return new AccountDto(
                account.getId(),
                account.getSerialId(),
                account.getOwnerId(),
                account.getAccount_name(),
                "",
                account.getTaxIdentificationNumber(),
                account.getCommercialRegistry(),
                account.getAddress(),
                account.getPhone(),
                account.getEmail(),
                account.getWebsite(),
                account.getPartner() == null ? null : account.getPartner().getId(),
                account.getParent() == null ? null : account.getParent().getId(),
                account.getCurrencyId(),
                account.getStateId(),
                account.getCountryId(),
                account.getAccountColor());

    }

    @Override
    public Account fromAccountDto(AccountDto accountDto) {
        if ( accountDto == null ) {
            return null;
        }
        Account.AccountBuilder account = Account.builder();

        account.serialId( accountDto.getSerialId() );
        account.account_name( accountDto.getAccountName() );
     //   account.account_logo( accountDto.getAccount_logo() );
     //   account.account_image( accountDto.getAccount_image() );
     //   account.min_value( accountDto.getMin_value() );
     //   account.min_quan( accountDto.getMin_quan() );
     //   account.credit( accountDto.getCredit() );
     //   account.rating( accountDto.getRating() );
        account.currencyId( accountDto.getCurrencyId() );
     //   account.delivery_period( accountDto.getDelivery_period() );
     //   account.weekend( accountDto.getWeekend() );
     //   account.work_hours( accountDto.getWork_hours() );

        return account.build();
    }

    @Override
    public CompanyHierarchyDto toHierarchyDto(Account a) {

            CompanyHierarchyDto dto = new CompanyHierarchyDto();

            dto.setSerialId(a.getSerialId());
            dto.setOwnerId(a.getOwnerId());
            dto.setAccountName(a.getAccount_name());
            dto.setTaxIdentificationNumber(a.getTaxIdentificationNumber());
            dto.setCommercialRegistry(a.getCommercialRegistry());
            dto.setAddress(a.getAddress());
            dto.setPhone(a.getPhone());
            dto.setEmail(a.getEmail());
            dto.setWebsite(a.getWebsite());
            dto.setColorAccount(a.getAccountColor());

            dto.setPartnerId(a.getId());
            dto.setParentId(
                    a.getParent() != null ? a.getParent().getId() : null
            );

            return dto;
        }

}
