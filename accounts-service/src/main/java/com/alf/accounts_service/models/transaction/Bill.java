package com.alf.accounts_service.models.transaction;


import com.alf.accounts_service.audit.Auditable;
import com.alf.accounts_service.models.Account;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bills", uniqueConstraints = {
        @UniqueConstraint(columnNames = "bill_number")
})
public class Bill extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bills_id_seq")
    @SequenceGenerator(name = "bills_id_seq", sequenceName = "bills_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "bill_number", length = 50, nullable = false, unique = true)
    private String billNumber;

    private Integer partner_id;

    @Column(name = "bill_date", nullable = false)
    private LocalDate billDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Column(name = "total_amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @OneToMany(mappedBy = "bill")
    private Set<BillLine> billLineSet = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "journal_entry_id")
    private JournalEntry journalEntry;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

}