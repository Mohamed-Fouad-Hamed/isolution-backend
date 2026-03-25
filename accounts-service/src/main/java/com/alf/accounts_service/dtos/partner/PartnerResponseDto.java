package com.alf.accounts_service.dtos.partner;

import com.alf.accounts_service.models.enums.CompanyType;
import com.alf.accounts_service.models.enums.PartnerType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PartnerResponseDto {

    private Long id;
    private String serialId;

    private String name;
    private String displayName;

    private Boolean isCompany;
    private CompanyType companyType;
    private PartnerType type;

    private Long parentId;
    private Long commercialPartnerId;

    private Integer customerRank;
    private Integer supplierRank;
    private Boolean employee;

    private String street;
    private String street2;
    private String city;
    private String zip;
    private Integer stateId;
    private Integer countryId;

    private String email;
    private String phone;
    private String mobile;
    private String website;

    private Long propertyAccountReceivableId;
    private Long propertyAccountPayableId;

    private String vat;
    private BigDecimal creditLimit;
    private Integer currencyId;

    private String lang;
    private String tz;

    private String companyName;
    private String commercialCompanyName;
    private Integer industryId;

    private Integer propertyPaymentTermId;
    private Integer propertyProductPriceListId;
}
