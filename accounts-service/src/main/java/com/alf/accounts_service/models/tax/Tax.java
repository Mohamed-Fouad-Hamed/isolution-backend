package com.alf.accounts_service.models.tax;

import com.alf.accounts_service.models.coa.FinancialAccount;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "taxes")
@Data
public class Tax {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tax_id_seq")
    @SequenceGenerator(name = "tax_id_seq", sequenceName = "tax_id_seq", allocationSize=1)
    private Long id;

    @Column(nullable = false)
    private String serialId;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal rate;

    @Column(nullable = false)
    private boolean active = true;

    @ManyToOne
    @JoinColumn(name = "tax_type_id", nullable = false)
    private TaxType taxType;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private FinancialAccount account;
}
