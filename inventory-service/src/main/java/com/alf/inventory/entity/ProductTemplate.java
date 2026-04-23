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

    @Column(name="product_type")
    @Enumerated(EnumType.STRING)
    private ProductType productType;

    @Column(name="company_id")
    private Long companyId;

    @Column(name = "category_id")
    private Long categoryId;


    @Column(name = "uom_id")
    private Long uomId;

    @Column(name = "purchase_uom_id")
    private Long purchaseUomId;

    @Column(name = "sales_uom_id")
    private Long saleUomId;

    private BigDecimal weight;

    private BigDecimal volume;

    private Boolean active = true;


}


