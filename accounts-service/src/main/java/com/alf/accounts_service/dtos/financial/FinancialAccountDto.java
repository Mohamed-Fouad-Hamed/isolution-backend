package com.alf.accounts_service.dtos.financial;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FinancialAccountDto {
    private Integer id;
    private String serialId;
    private String name;
    private String description;
    private String parentSerialId;
    private Integer accountTypeId; // ID of the type
    private String accountTypeName; // Denormalized name for convenience
    private String accountCategory;
    private BigDecimal balance;
    private String currency;
    private Boolean isActive;
    private boolean isDebit;
    private boolean isCashAccount;
    private boolean isBankAccount;
    private boolean isControlAccount;

}