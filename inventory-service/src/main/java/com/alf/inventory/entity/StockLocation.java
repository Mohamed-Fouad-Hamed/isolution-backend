package com.alf.inventory.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name="stock_location")
@Getter
public class StockLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String location_type;

    @Column(name="warehouse_id")
    private Long warehouseId;

    @Column(name="company_id")
    private Long companyId;

    @Column(name="partner_id")
    private Long partnerId;

    @Column(name="parent_id")
    private Long parentId;

}
