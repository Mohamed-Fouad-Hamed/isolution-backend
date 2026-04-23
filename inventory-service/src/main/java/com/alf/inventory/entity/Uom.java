package com.alf.inventory.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "uom")
@Getter
@Setter
@NoArgsConstructor
public class Uom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(name="company_id")
    private Long companyId;

    @JoinColumn(name = "group_id")
    private Long groupId;

    private BigDecimal factor;

    private BigDecimal rounding;

    private Boolean isReference;

    private Boolean active = true;
}

