package com.alf.accounts_service.dtos.type;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CursorPageTypeResponse<T> {
    private List<T> data;
    private TypeCursor nextCursor;
    private boolean hasNext;
}

