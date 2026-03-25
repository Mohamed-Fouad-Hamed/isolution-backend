package com.alf.accounts_service.models.core;


import com.alf.accounts_service.audit.Auditable;
import com.alf.accounts_service.models.Account;
import com.alf.accounts_service.models.enums.CompanyType;
import com.alf.accounts_service.models.enums.PartnerType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;



/*Partner (Company)
 ├── Branch (Company)
 ├── Contact (Person)
 ├── Invoice Address
 ├── Delivery Address
 └── Commercial Partner (Root)*/

@Entity
@Table(
        name = "partner",
        indexes = {
                @Index(name = "idx_partner_serial_id", columnList = "serial_id"),
                @Index(name = "idx_partner_name", columnList = "name"),
                @Index(name = "idx_partner_parent", columnList = "parent_id"),
                @Index(name = "idx_partner_customer_rank", columnList = "customer_rank"),
                @Index(name = "idx_partner_supplier_rank", columnList = "supplier_rank")
        }
)

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("is_delete = false")
public class Partner extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "partner_id_seq")
    @SequenceGenerator(
            name = "partner_id_seq",
            sequenceName = "partner_id_seq",
            allocationSize = 1
    )
    private Long id;

    /* ================== Identity ================== */

    @Column(name = "serial_id", length = 50, nullable = false, unique = true)
    private String serialId;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "display_name", length = 255)
    private String displayName;

    /* ================== Relations ================== */
    /*
        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "account_id", nullable = false)
        private Account account;
    */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Partner parent;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commercial_partner_id")
    private Partner commercialPartner;

    /* ================== Company ================== */

    @Column(name = "is_company", nullable = false)
    private Boolean isCompany = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "company_type", length = 20)
    private CompanyType companyType;

    /* ================== Ranks ================== */

    @Column(name = "customer_rank", nullable = false)
    private Integer customerRank = 0;

    @Column(name = "supplier_rank", nullable = false)
    private Integer supplierRank = 0;

    @Column(name = "employee")
    private boolean employee = false;

    /* ================== Address ================== */

    @Column(length = 255)
    private String street;

    @Column(length = 255)
    private String street2;

    @Column(length = 100)
    private String city;

    @Column(length = 20)
    private String zip;

    @Column(name = "state_id")
    private Integer stateId;

    @Column(name = "country_id")
    private Integer countryId;

    /* ================== Contact ================== */

    @Column(length = 255)
    private String email;

    @Column(length = 50)
    private String phone;

    @Column(length = 50)
    private String mobile;

    @Column(length = 255)
    private String website;

    /* ================== Accounting ================== */

    @Column(name = "property_account_receivable_id")
    private Long propertyAccountReceivableId;

    @Column(name = "property_account_payable_id")
    private Long propertyAccountPayableId;

    @Column(length = 50)
    private String vat;

    @Column(
            name = "credit_limit",
            precision = 15,
            scale = 2
    )
    private BigDecimal creditLimit = BigDecimal.ZERO;

    @Column(name = "currency_id")
    private Integer currencyId;

    /* ================== Meta ================== */

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PartnerType type;

    @Column(length = 10)
    private String lang;

    @Column(length = 50)
    private String tz;

    /* ================== Company Extra ================== */

    @Column(name = "company_name", length = 255)
    private String companyName;

    @Column(name = "commercial_company_name", length = 255)
    private String commercialCompanyName;

    @Column(name = "industry_id")
    private Integer industryId;

    @Column(name = "property_payment_term_id")
    private Integer propertyPaymentTermId;

    @Column(name = "property_product_price_list_id")
    private Integer propertyProductPriceListId;

    /* ================== System ================== */

}


