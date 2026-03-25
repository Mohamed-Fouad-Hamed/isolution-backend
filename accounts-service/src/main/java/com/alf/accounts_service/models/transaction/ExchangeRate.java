package com.alf.accounts_service.models.transaction;

import com.alf.accounts_service.models.Currency;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "exchange_rates")
@Data
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exchange_rate_id_seq")
    @SequenceGenerator(name = "exchange_rate_id_seq", sequenceName = "exchange_rate_id_seq", allocationSize=1)
    private Long id;

    @Column(name="serial_id",nullable = false)
    private String serialId;

    @Column(name="valid_from",nullable = false)
    private LocalDateTime validFrom;

    @Column(nullable = false)
    private BigDecimal rate;

    @ManyToOne
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;

}
