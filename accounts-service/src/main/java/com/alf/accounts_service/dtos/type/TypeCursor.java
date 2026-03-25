package com.alf.accounts_service.dtos.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeCursor {
    private String lastSearchText;
    private Long lastId;
}
