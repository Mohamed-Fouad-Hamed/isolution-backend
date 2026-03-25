package com.alf.accounts_service.dtos.partner;

import com.alf.accounts_service.models.enums.CompanyType;
import com.alf.accounts_service.models.enums.PartnerType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PartnerCreateDto {

    private String mapId;

    private String serialId;

    /* Identity */
    private String name;

    private String displayName;

    /* Relations */
    private Long parentId;

    /* Company */
    private Boolean isCompany;
    private CompanyType companyType;

    /* Ranks */
    private Integer customerRank;
    private Integer supplierRank;
    private Boolean employee;

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
    private Long propertyAccountReceivableId;
    private Long propertyAccountPayableId;
    private String vat;
    private BigDecimal creditLimit;
    private Integer currencyId;

    /* Meta */
    private PartnerType type;
    private String lang;
    private String tz;

    /* Company Extra */
    private String companyName;
    private String commercialCompanyName;
    private Integer industryId;
    private Integer propertyPaymentTermId;
    private Integer propertyProductPriceListId;

    /* Hierarchy (اختياري) */
    private List<PartnerCreateDto> partners;
}

