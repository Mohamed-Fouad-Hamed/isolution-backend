package com.alf.accounts_service.dtos.partner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartnerCursor {
    private String lastName;
    private Long lastId;
}
