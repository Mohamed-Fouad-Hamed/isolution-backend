package com.alf.accounts_service.mappers;

import com.alf.accounts_service.dtos.costcenter.CostCenterDepartmentDto;
import com.alf.accounts_service.dtos.department.DepartmentCreateDto;
import com.alf.accounts_service.dtos.department.DepartmentDto;

import com.alf.accounts_service.dtos.department.DepartmentUpdateDto;
import com.alf.accounts_service.models.Account;
import com.alf.accounts_service.models.core.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

// Declare as a Spring component for injection
@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE) // Ignore unmapped fields
public interface DepartmentMapper {

    // --- Entity to DTO ---
    @Mapping(source = "account.id", target = "accountId") // Map company entity to its ID
    DepartmentDto toDto(Department department);

    List<DepartmentDto> toDtoList(List<Department> departments);

    // --- Create DTO to Entity ---
    // Ignore fields that are auto-generated, set by Auditable, or handled manually (company)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "account", ignore = true) // Company object will be set manually in service
    @Mapping(target = "costCenters", ignore = true)
    Department toEntity(DepartmentCreateDto createDto);

    // --- Update DTO to Entity ---
    // Ignore fields that shouldn't be updated from DTO or are handled manually
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "account", ignore = true) // Handle company update manually in service if allowed
    @Mapping(target = "costCenters", ignore = true)
    // Use this if you want PATCH-like behavior (nulls in DTO don't overwrite existing values)
    // @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(DepartmentUpdateDto updateDto, @MappingTarget Department department);



    CostCenterDepartmentDto toCostCenterDepartment(Department department);

    // Helper method if needed (optional)
    default Account mapAccountIdToCompany(Long accountId) {
        if (accountId == null) {
            return null;
        }
        // In a real scenario, you wouldn't create a new Company here.
        // The service layer will fetch the actual Company entity.
        // This default method is more illustrative or for cases where
        // you might just need a proxy/reference, but typically not used like this.
        Account account = new Account(); // Placeholder - DO NOT USE IN PRODUCTION LIKE THIS
        account.setId(accountId);       // Service layer should fetch the real entity
        return account;
    }
}