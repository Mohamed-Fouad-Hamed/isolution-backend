package com.alf.accounts_service.dtos.Industry;

import java.util.List;

public record IndustryDto(
        Long id,
        String code,
        String name,
        String description,
        Long parentId,
        Boolean active,
        List<IndustryDto> children
) {}