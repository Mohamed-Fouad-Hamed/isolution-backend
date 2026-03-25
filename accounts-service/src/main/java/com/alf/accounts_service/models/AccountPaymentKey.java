package com.alf.accounts_service.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class AccountPaymentKey implements Serializable {

    @Column(name="account_id")
    private Long accountId;

    @Column (name="payment_id")
    private Integer paymentId;

}