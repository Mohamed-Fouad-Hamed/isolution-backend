package com.alf.accounts_service.dtos.Industry;

public record CreateIndustryCommand(
        String code,
        String name,
        String description,
        Long parentId
) {}

