package com.alf.inventory.entity;

import com.alf.inventory.enums.LocationType;
import jakarta.persistence.*;

@Entity
@Table(name = "location",
        indexes = {
                @Index(name = "idx_location_company", columnList = "company_id"),
                @Index(name = "idx_location_usage", columnList = "location_type")
        })
public class Location {

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name="location_type",nullable = false)
    private LocationType locationType; // INTERNAL, CUSTOMER, SUPPLIER, TRANSIT

    @Column(name="parent_id")
    private Long parentId;

    @Column(name="warehouse_id")
    private Long warehouseId;

    @Column(name="company_id")
    private Long companyId;   // Internal only

    @Column(name="partner_id")
    private Long partnerId;   // External only

    @Column(name="priority")
    private Integer priority;

    @Column(name="is_leaf")
    private Boolean isLeaf ; // for bins
}

