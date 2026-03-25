package com.alf.accounts_service.models.coa;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "fin_account_types")
public class FinancialAccountType  {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fin_account_type_id_seq")
    @SequenceGenerator(name = "fin_account_type_id_seq", sequenceName = "fin_account_type_id_seq", allocationSize=1)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @OneToMany(mappedBy = "accountType")
    private Set<FinancialAccount> accounts = new HashSet<>();

}
