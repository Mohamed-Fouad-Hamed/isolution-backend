package com.alf.inventory.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "lot",
        indexes = {
                @Index(name = "idx_lot_product", columnList = "product_id")
        })
@Getter
@Setter
public class Lot {  // Batch or Serial Entity

    @Id
    @GeneratedValue
    private Long id;

    private String name; // Batch or Serial

    @Column(name="product_id")
    private Long productId;

    private LocalDate expiryDate;

    private Boolean isSerial; // true = serial, false = lot
}
