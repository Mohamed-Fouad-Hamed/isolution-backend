package com.alf.accounts_service.dtos.state;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StateDto {
    private Integer id;


    private String name;

    private Integer countryId;

    private String countryName;
}
