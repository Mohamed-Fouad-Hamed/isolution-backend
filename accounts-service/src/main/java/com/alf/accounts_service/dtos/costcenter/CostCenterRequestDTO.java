package com.alf.accounts_service.dtos.costcenter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CostCenterRequestDTO {

    private String name;


    private String description;

    private Integer departmentId;
}
