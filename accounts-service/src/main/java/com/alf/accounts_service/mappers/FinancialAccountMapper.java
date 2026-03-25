package com.alf.accounts_service.mappers;

import com.alf.accounts_service.dtos.financial.FinancialAccountCreateDto;
import com.alf.accounts_service.dtos.financial.FinancialAccountDto;
import com.alf.accounts_service.dtos.financial.FinancialAccountUpdateDto;
import com.alf.accounts_service.models.coa.FinancialAccount;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FinancialAccountMapper {

    @Mappings({
            @Mapping(source = "accountType.id", target = "accountTypeId"),
            @Mapping(source = "accountType.name", target = "accountTypeName") //, // Denormalize type name
            //  @Mapping(source = "account.id", target = "accountId")
    })
    FinancialAccountDto toDto(FinancialAccount entity);

    List<FinancialAccountDto> toDtoList(List<FinancialAccount> entities);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "balance", ignore = true), // Set default in entity or service
            @Mapping(target = "accountType", ignore = true), // Set manually in service
            @Mapping(target = "account", ignore = true), // Set manually in service
        //    @Mapping(target = "journalEntryLines", ignore = true)
    })
    FinancialAccount toEntity(FinancialAccountCreateDto dto);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "serialId", ignore = true), // Typically immutable
            @Mapping(target = "balance", ignore = true), // Balance updated by transactions
            @Mapping(target = "accountType", ignore = true), // Handle relationship update in service
            @Mapping(target = "account", ignore = true), // Account relationship usually doesn't change
         //   @Mapping(target = "journalEntryLines", ignore = true)
    })
    void updateEntityFromDto(FinancialAccountUpdateDto dto, @MappingTarget FinancialAccount entity);

}