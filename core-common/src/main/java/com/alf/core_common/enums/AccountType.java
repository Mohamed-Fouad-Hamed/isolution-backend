package com.alf.core_common.enums;

import java.util.HashMap;
import java.util.Map;

public enum AccountType {

    SYS_ADMINISTRATOR(0),
    TYPE_CONSUMER(1),
    TYPE_MARKET(2),
    TYPE_RESTAURANT(3),
    TYPE_MEDICINE(4);
    private int value;
    private static Map mapInt = new HashMap<>();
    private static Map mapString = new HashMap<>();

    private AccountType(int value) {
        this.value = value;
    }

    static {
        for (AccountType accountType : AccountType.values()) {
            mapInt.put(accountType.value, accountType);
            mapString.put(accountType.name(), accountType);
        }
    }

    public static AccountType valueOf(int accountType) {
        return (AccountType) mapInt.get(accountType);
    }

    public static AccountType valueOfString(String accountType) {
        return (AccountType) mapString.get(accountType);
    }
    public int getValue() {
        return value;
    }
}
