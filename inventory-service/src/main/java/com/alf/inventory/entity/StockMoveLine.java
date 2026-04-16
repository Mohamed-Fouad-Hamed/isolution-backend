package com.alf.inventory.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

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

    @Column(name="product_id")
    private Long productId;

    @Column(name="source_location_id")
    private Long sourceLocationId;

    @Column(name="destination_location_id")
    private Long destinationLocationId;

    @Column(name="lot_id")
    private Long lotId;

    private BigDecimal qtyDone;
}

