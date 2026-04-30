package com.alf.inventory.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_move_line")
@Getter
@Setter
public class StockMoveLine {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name="move_id")
    private Long moveId;

    @Column(name="company_id")
    private Long  companyId;

    @Column(name="product_id")
    private Long productId;

    @Column(name="source_location_id")
    private Long sourceLocationId;

    @Column(name="destination_location_id")
    private Long destinationLocationId;

    @Column(name="lot_id")
    private Long lotId;

    @Column(name="reserve_quantity")
    private BigDecimal reserveQuantity;

    @Column(name="quantity_done")
    private BigDecimal quantityDone;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}

