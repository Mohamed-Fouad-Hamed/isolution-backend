package com.alf.accounts_service.mappers;

import com.alf.accounts_service.dtos.partner.PartnerCreateDto;
import com.alf.accounts_service.dtos.partner.PartnerResponseDto;
import com.alf.accounts_service.dtos.partner.PartnerUpdateDto;
import com.alf.accounts_service.models.core.Partner;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PartnerMapper {

    @Mapping(source = "parent.id", target = "parentId")
    PartnerResponseDto toDto(Partner entity);

    List<PartnerResponseDto> toDtoList(List<Partner> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "serialId", ignore = true)
    @Mapping(target = "commercialPartner", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "creditLimit", defaultExpression = "java(java.math.BigDecimal.ZERO)")
    Partner toEntity(PartnerCreateDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(PartnerUpdateDto dto, @MappingTarget Partner entity);
}

