package com.alf.accounts_service.dtos.partner;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PartnerUpdateDto {

    /* Identity */
    private String name;
    private String displayName;

    /* Address */
    private String street;
    private String street2;
    private String city;
    private String zip;
    private Integer stateId;
    private Integer countryId;

    /* Contact */
    private String email;
    private String phone;
    private String mobile;
    private String website;

    /* Accounting */
    private String vat;
    private BigDecimal creditLimit;
    private Integer currencyId;

    /* Meta */
    private String lang;
    private String tz;

    /* Company Extra */
    private String companyName;
    private String commercialCompanyName;
    private Integer industryId;
}

