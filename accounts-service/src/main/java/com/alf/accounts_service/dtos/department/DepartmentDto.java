package com.alf.accounts_service.dtos.department;

import lombok.Data;

@Data
public class DepartmentDto {
    private Long id;
    private String serialId;
    private String parentSerialId; // Nullable if it's a root department
    private String name;
    private String description;
    private Long accountId; // Include account ID for reference
}
