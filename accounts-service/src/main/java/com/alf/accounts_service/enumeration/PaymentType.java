package com.alf.accounts_service.enumeration;

import java.util.HashMap;
import java.util.Map;

public enum PaymentType {

    CASH(0),
    CREDIT_APP(1),
    CREDIT_CARD(2),
    WALLET(3),
    INSTALLMENT(4);
    private int value;
    private static Map mapInt = new HashMap<>();
    private static Map mapString = new HashMap<>();

    private PaymentType(int value) {
        this.value = value;
    }

    static {
        for (PaymentType paymentType : PaymentType.values()) {
            mapInt.put(paymentType.value, paymentType);
            mapString.put(paymentType.name(), paymentType);
        }
    }

    public static PaymentType valueOf(int paymentType) {
        return (PaymentType) mapInt.get(paymentType);
    }

    public static PaymentType valueOfString(String paymentType) {
        return (PaymentType) mapString.get(paymentType);
    }
    public int getValue() {
        return value;
    }
}