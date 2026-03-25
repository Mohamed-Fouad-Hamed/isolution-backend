package com.alf.accounts_service.models.coa;


import com.alf.accounts_service.audit.Auditable;
import com.alf.accounts_service.models.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;


@Entity
@Table(name = "fin_accounts")
@Getter
@Setter
@SQLRestriction("is_delete = false")
public class FinancialAccount extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fin_accounts_id_seq")
    @SequenceGenerator(name = "fin_accounts_id_seq", sequenceName = "fin_accounts_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "serial_id", length = 50, nullable = false, unique = true)
    private String serialId;

    // related main account
    @ManyToOne
    @JoinColumn(name = "m_account_id", nullable = false)
    private Account account;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "parent_serial_id", length = 50)
    private String parentSerialId;

    @ManyToOne
    @JoinColumn(name = "account_type_id")
    private FinancialAccountType accountType;

    @Column(name = "account_category", length = 50)
    private String accountCategory;

    @Column(precision = 15, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "currency_id")
    private Integer currencyId ;

    @Column(name = "is_active")
    private Boolean isActive = true;




    @Column(name = "is_debit")
    private boolean isDebit = true;
    @Column(name = "is_cash_account")
    private boolean isCashAccount = false;
    @Column(name = "is_bank_account")
    private boolean isBankAccount = false;
    @Column(name = "is_control_account")
    private boolean isControlAccount = false;
}