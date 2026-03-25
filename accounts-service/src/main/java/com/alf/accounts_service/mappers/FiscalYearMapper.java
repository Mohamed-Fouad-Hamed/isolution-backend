package com.alf.accounts_service.mappers;


import com.alf.accounts_service.dtos.fiscalperiod.FiscalYearRequestDTO;
import com.alf.accounts_service.dtos.fiscalperiod.FiscalYearResponseDTO;
import com.alf.accounts_service.models.core.FiscalYear;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FiscalYearMapper {

    @Mapping(target = "id", ignore = true) // Handled by DB
    @Mapping(target = "serialId", ignore = true) // Handled by service
    @Mapping(target = "closed", ignore = true) // Default to false in entity
    @Mapping(target = "account", ignore = true) // Will be set manually from accountId
    FiscalYear toEntity(FiscalYearRequestDTO dto);

    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "account.account_name", target = "accountName")
    FiscalYearResponseDTO toResponseDTO(FiscalYear fiscalYear);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "serialId", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "closed", ignore = true) // Closed status handled by specific endpoints
    void updateEntityFromDto(FiscalYearRequestDTO dto, @MappingTarget FiscalYear fiscalYear);
}