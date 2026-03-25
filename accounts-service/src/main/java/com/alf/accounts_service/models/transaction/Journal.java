package com.alf.accounts_service.models.transaction;


import com.alf.accounts_service.models.Account;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
@Entity
@Table(name = "journals")
@Data
public class Journal {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "journal_id_seq")
    @SequenceGenerator(name = "journal_id_seq", sequenceName = "journal_id_seq", allocationSize=1)
    private Long id;

    @Column(name="serial_id",nullable = false)
    private String serialId;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "journal")
    private Set<JournalEntry> journalEntries = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "m_account_id", nullable = false)
    private Account account;
}