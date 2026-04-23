package com.alf.inventory.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "uom_group")
@Getter
@Setter
@NoArgsConstructor
public class UomGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(name="company_id")
    private Long companyId;

    private String description;

    private Boolean active = true;

    private LocalDateTime createdAt;
}
