package com.alf.accounts_service.dtos.partner;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CursorPageResponse<T> {
    private List<T> data;
    private PartnerCursor nextCursor;
    private boolean hasNext;
}

