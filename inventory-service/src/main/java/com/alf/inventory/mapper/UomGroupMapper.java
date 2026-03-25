package com.alf.inventory.mapper;

import com.alf.inventory.dto.UomGroupDTO;
import com.alf.inventory.entity.UomGroup;

public class UomGroupMapper {

    public static UomGroupDTO toDto(UomGroup e){
        return new UomGroupDTO(
                e.getId(),
                e.getCode(),
                e.getName()
        );
    }

}

