package com.alf.accounts_service.models;

import com.alf.accounts_service.models.enums.DueType;
import com.alf.accounts_service.models.enums.ValueType;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "payment_term_line")
public class PaymentTermLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_term_id")
    private PaymentTerm paymentTerm;

    @Enumerated(EnumType.STRING)
    @Column(name = "value_type", nullable = false)
    private ValueType valueType;

    @Column(name = "value_amount", precision = 10, scale = 2)
    private BigDecimal valueAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "due_type", nullable = false)
    private DueType dueType;

    private Integer days = 0;

    private Integer sequence = 1;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PaymentTerm getPaymentTerm() {
        return paymentTerm;
    }

    public void setPaymentTerm(PaymentTerm paymentTerm) {
        this.paymentTerm = paymentTerm;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public void setValueType(ValueType valueType) {
        this.valueType = valueType;
    }

    public BigDecimal getValueAmount() {
        return valueAmount;
    }

    public void setValueAmount(BigDecimal valueAmount) {
        this.valueAmount = valueAmount;
    }

    public DueType getDueType() {
        return dueType;
    }

    public void setDueType(DueType dueType) {
        this.dueType = dueType;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }
}

