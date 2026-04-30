package com.alf.inventory.entity;

import com.alf.inventory.enums.PickingLineState;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "picking_line",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"picking_id", "move_line_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PickingLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="picking_id",nullable = false)
    private Long pickingId;

    @Column(name="move_line_id",nullable = false)
    private Long moveLineId;

    @Column(name="product_id",nullable = false)
    private Long productId;

    @Column(name="source_location_id",nullable = false)
    private Long sourceLocationId;

    @Column(name="destination_location_id",nullable = false)
    private Long destinationLocationId;

    @Column(name="lot_id")
    private Long lotId;

    @Column(name="reserved_qty",nullable = false, precision = 16, scale = 6)
    private BigDecimal reservedQty;

    @Column(name="picked_qty",nullable = false, precision = 16, scale = 6)
    private BigDecimal pickedQty = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PickingLineState state;

    @Column(name="scanned_barcode")
    private String scannedBarcode;

    @Column(name="picked_at")
    private LocalDateTime pickedAt;

    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;

    public boolean fullyPicked() {
        return pickedQty.compareTo(reservedQty) == 0;
    }


}
