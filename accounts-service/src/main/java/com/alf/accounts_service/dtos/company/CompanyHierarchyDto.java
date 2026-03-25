package com.alf.accounts_service.dtos.company;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class CompanyHierarchyDto {
    private String serialId;
    private String ownerId;
    private String accountName;
    private String taxIdentificationNumber;
    private String commercialRegistry;
    private String address;
    private String phone;
    private String email;
    private String website;

    private Long partnerId;
    private Long parentId;
    private String colorAccount;

    private List<CompanyHierarchyDto> branches = new ArrayList<>();
}
