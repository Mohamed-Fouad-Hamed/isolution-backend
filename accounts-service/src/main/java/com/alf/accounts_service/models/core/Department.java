package com.alf.accounts_service.models.core;


import com.alf.accounts_service.audit.Auditable;
import com.alf.accounts_service.models.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "departments")
@Getter
@Setter
@SQLRestriction("is_delete = false")
public class Department extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "department_id_seq")
    @SequenceGenerator(name = "department_id_seq", sequenceName = "department_id_seq", allocationSize=1)
    private Integer id;

    @Column(name="serial_id",nullable = false)
    private String serialId;

    @Column(name="parent_serial_id")
    private String parentSerialId;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @OneToMany(mappedBy = "department")
    private Set<CostCenter> costCenters = new HashSet<>();
}

