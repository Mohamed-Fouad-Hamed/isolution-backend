package com.alf.inventory.entity;

import com.alf.inventory.enums.TrackingType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TrackingType tracking; // NONE, LOT, SERIAL

    @Column(name = "template_id")
    private Long templateId;

    @Column(unique = true)
    private String sku;

    private String barcode;

    private Boolean active = true;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductResourceMapping> resourceMappings = new ArrayList<>();
}
