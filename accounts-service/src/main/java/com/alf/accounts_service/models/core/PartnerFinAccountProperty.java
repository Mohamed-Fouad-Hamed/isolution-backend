package com.alf.accounts_service.models.core;

import com.alf.accounts_service.models.Account;
import com.alf.accounts_service.models.coa.FinancialAccount;
import com.alf.accounts_service.models.enums.FinAccountPropertyType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "partner_fin_account_property",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "partner_id",
                                "account_id",
                                "property_type"
                        }
                )
        }
)
@Getter
@Setter
public class PartnerFinAccountProperty {

    @Id
    @GeneratedValue
    private Long id;

    // العميل / المورد
    @ManyToOne(optional = false)
    private Partner partner;

    // الشركة
    @ManyToOne(optional = false)
    private Account account;

    // نوع الحساب
    @Enumerated(EnumType.STRING)
    @Column(name = "property_type", length = 30)
    private FinAccountPropertyType finAccountPropertyType;
    // RECEIVABLE, PAYABLE

    // الحساب المحاسبي
    @ManyToOne(optional = false)
    private FinancialAccount finAccount;
}

