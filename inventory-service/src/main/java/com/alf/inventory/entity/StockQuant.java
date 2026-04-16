package com.alf.inventory.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "stock_quant",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"product_id", "location_id", "lot_id"}
        ),
        indexes = {
                @Index(name = "idx_quant_product", columnList = "product_id"),
                @Index(name = "idx_quant_location", columnList = "location_id"),
                @Index(name = "idx_quant_company", columnList = "company_id")
        })
@Getter
@Setter
public class StockQuant {

    @Id
    @GeneratedValue
    private Long id;

    @Version
    private Long version;

    @Column(name="company_id")
    private Long  companyId;

    @Column(name="product_id")
    private Long productId;

    @Column(name="location_id")
    private Long locationId;

    @Column(name="lot_id")
    private Long lotId;


    @Column(nullable = false)
    private BigDecimal quantity = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal reservedQuantity = BigDecimal.ZERO;

    public BigDecimal getAvailable() {
        return quantity.subtract(reservedQuantity);
    }

    public void reserve(BigDecimal qty) {
        if (getAvailable().compareTo(qty) < 0) {
            throw new IllegalStateException("Not enough stock");
        }
        this.reservedQuantity = this.reservedQuantity.add(qty);
    }

}

