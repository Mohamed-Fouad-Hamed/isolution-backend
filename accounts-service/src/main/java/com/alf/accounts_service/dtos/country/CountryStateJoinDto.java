package com.alf.accounts_service.dtos.country;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryStateJoinDto {
    private Integer countryId;
    private String countryName;
    private Integer stateId;
    private String stateName;
}
