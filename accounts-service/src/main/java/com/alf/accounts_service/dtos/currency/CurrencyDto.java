package com.alf.accounts_service.dtos.currency;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CurrencyDto {

    private Integer id;

    private String serialId;

    private String code;

    private String name;

    private String symbol;

    private boolean active = true;
}
