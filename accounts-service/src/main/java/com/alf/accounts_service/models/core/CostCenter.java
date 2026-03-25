package com.alf.accounts_service.models.core;

import com.alf.accounts_service.audit.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;


@Getter
@Setter
@Entity
@Table(name = "cost_centers")
@SQLRestriction("is_delete = false")
public class CostCenter extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cost_center_id_seq")
    @SequenceGenerator(name = "cost_center_id_seq", sequenceName = "cost_center_id_seq", allocationSize=1)
    private Integer id;

    @Column(name="serial_id",nullable = false)
    private String serialId;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}
