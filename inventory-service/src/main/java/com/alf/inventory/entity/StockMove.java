package com.alf.inventory.entity;

import com.alf.inventory.enums.MoveState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "stock_move",
        indexes = {
                @Index(name = "idx_move_product", columnList = "product_id"),
                @Index(name = "idx_move_company", columnList = "company_id")
        })
@Getter
@Setter
public class StockMove {


    @Id @GeneratedValue
    private Long id;

    @Column(name="company_id")
    private Long  companyId;

    @Column(name="product_id")
    private Long productId;

    @Column(name="source_location_id")
    private Long sourceLocationId;

    @Column(name="destination_location_id")
    private Long destinationLocationId;

    private BigDecimal quantity;

    @Enumerated(EnumType.STRING)
    private MoveState state;

}
