package com.alf.inventory.entity;

import com.alf.inventory.enums.ProductType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "product_template")
@Getter
@Setter
@NoArgsConstructor
public class ProductTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ProductType productType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private ProductCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uom_id")
    private Uom uom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_uom_id")
    private Uom purchaseUom;

    private BigDecimal weight;

    private BigDecimal volume;

    private Boolean active = true;

    @OneToMany(mappedBy = "template")
    private List<Product> variants;

}


