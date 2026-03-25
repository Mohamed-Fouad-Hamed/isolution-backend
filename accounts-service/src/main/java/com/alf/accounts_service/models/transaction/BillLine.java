package com.alf.accounts_service.models.transaction;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "bill_lines")
public class BillLine {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bill_lines_id_seq")
    @SequenceGenerator(name = "bill_lines_id_seq", sequenceName = "bill_lines_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bill_id")
    private Bill bill;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "quantity", precision = 15, scale = 2, nullable = false)
    private BigDecimal quantity;

    @Column(name = "unit_price", precision = 15, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "fin_account_id")
    private String financialAccountId;
}
