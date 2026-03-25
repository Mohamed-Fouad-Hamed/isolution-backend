package com.alf.accounts_service.models.tax;


import com.alf.accounts_service.models.Account;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "tax_types")
@Data
public class TaxType {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tax_type_id_seq")
    @SequenceGenerator(name = "tax_type_id_seq", sequenceName = "tax_type_id_seq", allocationSize=1)
    private Long id;

    @Column(nullable = false)
    private String serialId;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @OneToMany(mappedBy = "taxType")
    private Set<Tax> taxes = new HashSet<>();

}
