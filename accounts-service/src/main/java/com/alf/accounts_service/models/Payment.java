package com.alf.accounts_service.models;

import com.alf.accounts_service.enumeration.PaymentType;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "app_payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_payments_id_seq")
    @SequenceGenerator(name = "app_payments_id_seq", sequenceName = "app_payments_id_seq", allocationSize=1)
    @Column(name = "id")
    private Integer id;

    @Column(name="name",nullable = false, length = 100)
    private String name;

    @Column(name = "payment_type",nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @OneToMany(mappedBy = "payment")
    private Set<AccountPayment> accountPaymentSet = new HashSet<>();

}
