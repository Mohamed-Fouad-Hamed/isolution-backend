package com.alf.accounts_service.dtos.department;


import lombok.Data;

@Data
public class DepartmentCreateDto {


    private String serialId;


    private String parentSerialId;


    private String name;


    private String description;


    private Long accountId; // ID of the account this department belongs to
}