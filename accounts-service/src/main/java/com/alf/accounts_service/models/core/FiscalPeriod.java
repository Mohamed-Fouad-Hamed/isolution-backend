package com.alf.accounts_service.models.core;


import com.alf.accounts_service.audit.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "fiscal_periods")
@Getter
@Setter
@SQLRestriction("is_delete = false")
public class FiscalPeriod extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fiscal_period_seq")
    @SequenceGenerator(name = "fiscal_period_seq", sequenceName = "fiscal_period_seq", allocationSize=1)
    private Integer id;

    @Column(name="serial_id",nullable = false)
    private String serialId;

    @Column(nullable = false)
    private String name;

    @Column(name="start_date",nullable = false)
    private LocalDateTime startDate;

    @Column(name="end_date",nullable = false)
    private LocalDateTime endDate;

    @Column(name="is_closed",nullable = false)
    private boolean closed = false;

    @ManyToOne
    @JoinColumn(name = "fiscal_year_id", nullable = false)
    private FiscalYear fiscalYear;
}