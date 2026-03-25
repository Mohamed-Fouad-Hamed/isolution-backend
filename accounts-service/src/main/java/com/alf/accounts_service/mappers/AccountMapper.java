package com.alf.accounts_service.mappers;


import com.alf.accounts_service.dtos.company.CompanyHierarchyDto;
import com.alf.accounts_service.models.Account;
import com.alf.accounts_service.dtos.account.AccountDto;

public interface AccountMapper {
    AccountDto toAccountDto(Account account);
    Account fromAccountDto(AccountDto accountDto);

    CompanyHierarchyDto toHierarchyDto(Account account);
}
