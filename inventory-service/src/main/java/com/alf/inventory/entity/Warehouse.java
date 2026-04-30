package com.alf.inventory.entity;

import com.alf.inventory.enums.AllocationMethod;
import com.alf.inventory.enums.MoveType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="warehouse")
@Getter
@Setter
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String name;

    @Column(name="company_id")
    private Long companyId;

    @Enumerated(EnumType.STRING)
    @Column(name="allocation_method")
    private AllocationMethod allocationMethod;

    @Column(name="allow_partial_reservation")
    private Boolean allowPartialReservation;

}

