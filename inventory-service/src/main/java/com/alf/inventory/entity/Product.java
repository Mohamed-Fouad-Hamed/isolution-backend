package com.alf.inventory.entity;

import com.alf.inventory.enums.TrackingType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    private ProductTemplate template;

    @Column(unique = true)
    private String sku;

    private String barcode;

    private Boolean active = true;
}
