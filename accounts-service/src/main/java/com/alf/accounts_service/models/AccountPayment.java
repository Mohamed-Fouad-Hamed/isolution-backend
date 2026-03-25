package com.alf.accounts_service.models;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "account_payments")
public class AccountPayment {

    @EmbeddedId
    private AccountPaymentKey id;

    @ManyToOne
    @MapsId("accountId")
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @MapsId("paymentId")
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Column(name="wallet_number", length = 16)
    private String wallet_number;

    @Column(name="wallet_password", length = 30)
    private String wallet_password;

    @Column(name="card_holder_name", length = 100)
    private String card_holder_name;

    @Column(name="account_number", length = 16)
    private String account_number;

    @Column(name="expiry_month", length = 2)
    private String expiry_month;

    @Column(name="expiry_year", length = 2)
    private String expiry_year;

    @Column(name="cvc", length = 4)
    private String cvc;




}
