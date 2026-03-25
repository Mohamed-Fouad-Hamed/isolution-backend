package com.alf.accounts_service.mappers;

import com.alf.accounts_service.dtos.country.CountryDto;
import com.alf.accounts_service.dtos.state.StateDto;
import com.alf.accounts_service.models.Country;
import com.alf.accounts_service.models.State;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CountryMappingService {
    public CountryDto toCountryDto(Country country) {
        CountryDto dto = new CountryDto();
        dto.setId(country.getId());
        dto.setName(country.getName());
        return dto;
    }

    public CountryDto toCountryDtoWithStates(Country country) {
        CountryDto dto = toCountryDto(country);
        if (country.getStates() != null) {
            dto.setStates(country.getStates().stream()
                    .map(this::toStateDto)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    public StateDto toStateDto(State state) {
        StateDto dto = new StateDto();
        dto.setId(state.getId());
        dto.setName(state.getName());
        if (state.getCountry() != null) {
            dto.setCountryId(state.getCountry().getId());
            dto.setCountryName(state.getCountry().getName());
        }
        return dto;
    }

    public Country toCountryEntity(CountryDto dto) {
        Country country = new Country();
        country.setId(dto.getId());
        country.setName(dto.getName());
        return country;
    }

    public State toStateEntity(StateDto dto, Country country) {
        State state = new State();
        state.setId(dto.getId());
        state.setName(dto.getName());
        state.setCountry(country);
        return state;
    }

}
