package com.alf.accounts_service.dtos.financial;


import lombok.Data;

@Data
public class FinancialAccountCreateDto {


        private String serialId;


        private String name;

        private String description;

        private String parentSerialId; // Optional

        private Integer accountTypeId;

       private String accountCategory;

        // Balance typically starts at 0 and is modified by transactions, not direct creation.
        // private BigDecimal initialBalance; // Optional: if you allow setting initial balance

        private Integer currencyId; // Default can be set here or in service

        private Boolean isActive = true; // Default

        // accountId will be taken from the path parameter in the controller/service

        private boolean isDebit = true; // Default
        private boolean isCashAccount = false;
        private boolean isBankAccount = false;
        private boolean isControlAccount = false;
}
