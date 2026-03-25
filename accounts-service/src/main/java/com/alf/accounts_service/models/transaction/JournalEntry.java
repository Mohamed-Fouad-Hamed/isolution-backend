package com.alf.accounts_service.models.transaction;


import com.alf.accounts_service.audit.Auditable;
import com.alf.accounts_service.models.Currency;
import com.alf.accounts_service.models.core.FiscalPeriod;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "journal_entries")
@Data
public class JournalEntry extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "journal_entry_id_seq")
    @SequenceGenerator(name = "journal_entry_id_seq", sequenceName = "journal_entry_id_seq", allocationSize=1)
    private Long id;

    @Column(name="serial_id",nullable = false)
    private String serialId;

    @Column(nullable = false, unique = true)
    private String referenceNumber;

    @Column(nullable = false)
    private LocalDateTime entryDate;

    private String description;

    @Column(nullable = false)
    private boolean posted = false;

    @ManyToOne
    @JoinColumn(name = "fiscal_period_id", nullable = false)
    private FiscalPeriod fiscalPeriod;

    @ManyToOne
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;

    @Column(nullable = false)
    private BigDecimal exchangeRate = BigDecimal.ONE;

    @ManyToOne
    @JoinColumn(name = "journal_id", nullable = false)
    private Journal journal;

    @OneToMany(mappedBy = "journalEntry", cascade = CascadeType.ALL)
    private Set<JournalEntryLine> journalEntryLines = new HashSet<>();

}