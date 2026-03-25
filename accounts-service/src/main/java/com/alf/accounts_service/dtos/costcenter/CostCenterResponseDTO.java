package com.alf.accounts_service.dtos.costcenter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CostCenterResponseDTO {
    private Integer id;
    private String serialId;
    private String code;
    private String name;
    private String description;

    // Department Information
    private Integer departmentId;
    private String departmentName;
    private String departmentSerialId;
}
