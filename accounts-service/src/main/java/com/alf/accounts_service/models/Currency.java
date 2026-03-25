package com.alf.accounts_service.models;


import com.alf.accounts_service.models.transaction.ExchangeRate;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "currencies")
@Data
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "currency_id_seq")
    @SequenceGenerator(name = "currency_id_seq", sequenceName = "currency_id_seq", allocationSize=1)
    private Integer id;

    @Column(name="serial_id",nullable = false)
    private String serialId;

    @Column(nullable = false, unique = true, length = 3)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String symbol;

    @Column(nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "currency")
    private Set<ExchangeRate> exchangeRates = new HashSet<>();
}