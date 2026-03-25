package com.alf.accounts_service.dtos.department;


import lombok.Data;

@Data
public class DepartmentUpdateDto {

    // Usually serial_id is not updatable, but keeping it here if your business logic allows.
    // If not updatable, remove it or add validation in service layer.

    private String serialId;


    private String parentSerialId; // Allow changing parent


    private String name;


    private String description;

    // If departments cannot change companies, remove this field.
    // If they can, ensure the service layer handles the Company object update correctly.

    private Long accountId;
}