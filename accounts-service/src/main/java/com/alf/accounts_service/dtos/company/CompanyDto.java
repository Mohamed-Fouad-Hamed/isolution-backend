package com.alf.accounts_service.dtos.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyDto {
    private String serialId;
    private String taxIdentificationNumber;
    private String name ;
    private String phone;
    private String address ;
    private String email ;
    private String website ;
    private LocalDateTime created_date;
}
