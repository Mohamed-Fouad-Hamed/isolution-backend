package com.alf.accounts_service.dtos.partner;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PartnerSummaryDto {
    private Long id;
    private String name;
    private String serialId;
    private Boolean isCompany;
    private Integer customerRank;
    private Integer supplierRank;
}

