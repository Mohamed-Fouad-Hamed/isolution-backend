package com.alf.accounts_service.models.transaction;


import com.alf.accounts_service.models.coa.FinancialAccount;
import com.alf.accounts_service.models.core.CostCenter;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;



@Entity
@Table(name = "journal_entry_lines")
@Data
public class JournalEntryLine {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "journal_entry_line_id_seq")
    @SequenceGenerator(name = "journal_entry_line_id_seq", sequenceName = "journal_entry_line_id_seq", allocationSize=1)
    private Long id;

    @Column(nullable = false)
    private BigDecimal debit = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal credit = BigDecimal.ZERO;

    private String description;

    private Integer partner_id;

    @ManyToOne
    @JoinColumn(name = "journal_entry_id", nullable = false)
    private JournalEntry journalEntry;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private FinancialAccount account;

    @ManyToOne
    @JoinColumn(name = "cost_center_id")
    private CostCenter costCenter;
}