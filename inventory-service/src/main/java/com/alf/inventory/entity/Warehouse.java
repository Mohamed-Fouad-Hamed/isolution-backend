package com.alf.inventory.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="warehouse")
@Getter
@Setter
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String name;

    @Column(name="company_id")
    private Long companyId;

    // Default locations
    @OneToOne
    private Location inputLocation;

    @OneToOne
    private Location stockLocation;

    @OneToOne
    private Location outputLocation;

    @OneToMany(mappedBy = "warehouse")
    private List<Location> locations;

}

