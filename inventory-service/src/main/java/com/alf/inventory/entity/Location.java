package com.alf.inventory.entity;

import com.alf.inventory.enums.LocationType;
import jakarta.persistence.*;

@Entity
@Table(name = "location",
        indexes = {
                @Index(name = "idx_location_company", columnList = "company_id"),
                @Index(name = "idx_location_usage", columnList = "usage")
        })
public class Location {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LocationType usage; // INTERNAL, CUSTOMER, SUPPLIER, TRANSIT

    @ManyToOne
    private Warehouse warehouse;  // Optional for internal locations

    @Column(name="parent_id")
    private Long parentId;

    @Column(name="warehouse_id")
    private Long warehouseId;

    @Column(name="company_id")
    private Long companyId;   // Internal only

    @Column(name="partner_id")
    private Long partnerId;   // External only

    private Boolean isLeaf = true; // for bins
}

