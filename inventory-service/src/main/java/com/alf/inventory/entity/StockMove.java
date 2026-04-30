package com.alf.inventory.entity;

import com.alf.inventory.enums.MoveState;
import com.alf.inventory.enums.MoveType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @Column(name="uom_id")
    private Long uomId;

    private BigDecimal quantity;

    @Column(name="reserved_quantity")
    private BigDecimal reservedQuantity;

    @Enumerated(EnumType.STRING)
    @Column(name="move_type")
    private MoveType moveType;

    @Enumerated(EnumType.STRING)
    @Column(name="move_state")
    private MoveState moveState;

    private String reference ;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
