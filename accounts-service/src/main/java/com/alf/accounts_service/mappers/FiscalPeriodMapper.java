package com.alf.accounts_service.mappers;

import com.alf.accounts_service.dtos.fiscalperiod.FiscalPeriodRequestDTO;
import com.alf.accounts_service.dtos.fiscalperiod.FiscalPeriodResponseDTO;
import com.alf.accounts_service.models.core.FiscalPeriod;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FiscalPeriodMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "serialId", ignore = true) // Corrected from serial_id
    @Mapping(target = "closed", ignore = true)
    @Mapping(target = "fiscalYear", ignore = true)
    FiscalPeriod toEntity(FiscalPeriodRequestDTO dto);

    @Mapping(source = "fiscalYear.id", target = "fiscalYearId")
    @Mapping(source = "fiscalYear.name", target = "fiscalYearName")
    FiscalPeriodResponseDTO toResponseDTO(FiscalPeriod fiscalPeriod);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "serialId", ignore = true) // Corrected from serial_id
    @Mapping(target = "fiscalYear", ignore = true)
    @Mapping(target = "closed", ignore = true)
    void updateEntityFromDto(FiscalPeriodRequestDTO dto, @MappingTarget FiscalPeriod fiscalPeriod);
}