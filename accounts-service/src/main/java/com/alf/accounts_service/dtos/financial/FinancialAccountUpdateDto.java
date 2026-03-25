package com.alf.accounts_service.dtos.financial;



import lombok.Data;

@Data
public class FinancialAccountUpdateDto {

    // serialId is often immutable, consider removing if it cannot be changed.
    // If changeable, add validation in service layer.
    // @NotBlank @Size(max = 50)
    // private String serialId;


    private String name;

    private String description;


    private String parentSerialId; // Allow changing parent


    private Integer accountTypeId;


    private String accountCategory;

    // Balance should NOT be updatable via PUT typically. Updated via transactions.


    private String currency; // Allow currency change?


    private Boolean isActive;

    // accountId usually doesn't change for a financial account.


    private Boolean isDebit;

    private Boolean isCashAccount;


    private Boolean isBankAccount;


    private Boolean isControlAccount;

}
