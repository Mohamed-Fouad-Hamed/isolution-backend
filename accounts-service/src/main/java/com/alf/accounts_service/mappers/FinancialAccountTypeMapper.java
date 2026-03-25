package com.alf.accounts_service.mappers;

import com.alf.accounts_service.dtos.financial.FinancialAccountTypeDto;
import com.alf.accounts_service.models.coa.FinancialAccountType;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FinancialAccountTypeMapper {
    FinancialAccountTypeDto toFinancialAccountTypeDto(FinancialAccountType financialAccountType);
}
