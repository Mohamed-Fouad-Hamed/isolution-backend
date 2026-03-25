package com.alf.accounts_service.models;


import com.alf.accounts_service.audit.Auditable;
import com.alf.accounts_service.models.coa.FinancialAccount;
import com.alf.accounts_service.models.core.Department;
import com.alf.accounts_service.models.core.FiscalYear;
import com.alf.accounts_service.models.core.Partner;
import com.alf.accounts_service.models.enums.CompanyType;
import com.alf.accounts_service.models.transaction.Bill;
import com.alf.accounts_service.models.transaction.Invoice;
import com.alf.accounts_service.models.transaction.Journal;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "APP_ACCOUNTS")
@SQLRestriction("is_delete = false")
public class Account extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_accounts_id_seq")
    @SequenceGenerator(name = "app_accounts_id_seq", sequenceName = "app_accounts_id_seq", allocationSize=1)
    @Column(name = "id")
    private Long id;

    @Column(name = "serial_id",unique = true )
    private String serialId;

    @Column(name = "owner_id")
    private String ownerId;

    @Column(name="account_name", nullable = false, length = 50)
    private String account_name;

    @Enumerated(EnumType.STRING)
    @Column(name = "company_type", length = 20)
    private CompanyType companyType;

    @Column(name = "account_color",length = 10)
    private String accountColor;

    // modify for hierarchy accounts and main commercial account

    @Column(name = "tax_identification_number",unique = true)
    private String taxIdentificationNumber;

    @Column(name = "commercial_registry")
    private String commercialRegistry;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "website")
    private String website;

    /* Parent Company */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Account parent;

    /* Commercial Partner */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "partner_id", unique = true)
    private Partner partner;

    @Column(name="currency_id")
    private Integer currencyId;

    @Column(name="state_id")
    private Integer stateId ;

    @Column(name="country_id")
    private Integer countryId ;

    // related entity sections

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "default_receivable_account_id")
    private FinancialAccount defaultReceivableAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "default_payable_account_id")
    private FinancialAccount defaultPayableAccount;

    @OneToMany(fetch = FetchType.LAZY ,mappedBy = "account")
    private Set<AccountPayment> accountPaymentSet = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "account_places",
            joinColumns = {@JoinColumn(name = "account_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "place_id", referencedColumnName = "id")})
    Set<Place> placeSet;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private Set<Department> departments = new HashSet<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private Set<FiscalYear> fiscalYears = new HashSet<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private Set<FinancialAccount> financialAccounts = new HashSet<>();

    @OneToMany(mappedBy = "account")
    private Set<Journal> journalSet = new HashSet<>();

    @OneToMany(mappedBy = "account")
    private Set<Bill> billSet = new HashSet<>();

    @OneToMany(mappedBy = "account")
    private Set<Invoice> invoiceSet = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "account_currency",
            joinColumns = {@JoinColumn(name = "account_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "currency_id", referencedColumnName = "id")})
    private Set<Currency> currencySet = new HashSet<>();





}
