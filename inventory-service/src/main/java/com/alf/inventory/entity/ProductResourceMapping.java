package com.alf.inventory.entity;

import com.alf.inventory.enums.ResourceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "product_resource_mapping")
@Getter
@Setter
public class ProductResourceMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id")
    private Resource resource;

    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;
}
