package com.alf.accounts_service.dtos.type;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TypeLookupDto {
    private Long id ;
    private String name ;
    private String groupName ;
}
