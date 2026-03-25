package com.alf.accounts_service.dtos.country;


import com.alf.accounts_service.dtos.state.StateDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryDto {

    private Integer id;

    private String name;

    private List<StateDto> states;
}
