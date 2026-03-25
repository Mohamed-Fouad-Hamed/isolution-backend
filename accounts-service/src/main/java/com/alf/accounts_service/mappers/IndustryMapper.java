package com.alf.accounts_service.mappers;

import com.alf.accounts_service.dtos.Industry.IndustryDto;
import com.alf.accounts_service.models.Industry;

import java.util.stream.Collectors;

public class IndustryMapper {

    public static IndustryDto toDto(Industry entity) {
        return new IndustryDto(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getDescription(),
                entity.getParent() != null ? entity.getParent().getId() : null,
                entity.getActive(),
                entity.getChildren() != null
                        ? entity.getChildren()
                        .stream()
                        .map(IndustryMapper::toDto)
                        .collect(Collectors.toList())
                        : null
        );
    }
}
