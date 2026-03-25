package com.alf.accounts_service.services;


import com.alf.accounts_service.dtos.state.StateDto;
import com.alf.accounts_service.mappers.CountryMappingService;
import com.alf.accounts_service.models.Country;
import com.alf.accounts_service.models.State;
import com.alf.accounts_service.repositories.CountryRepository;
import com.alf.accounts_service.repositories.StateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StateService {

    private final StateRepository stateRepository;
    private final CountryRepository countryRepository;
    private final CountryMappingService mappingService;

    @Transactional(readOnly = true)
    public List<StateDto> getAllStates() {
        return stateRepository.findAll()
                .stream()
                .map(mappingService::toStateDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<StateDto> getStateById(Integer id) {
        State state = stateRepository.findByIdWithCountry(id);
        return state != null ? Optional.of(mappingService.toStateDto(state)) : Optional.empty();
    }

    @Transactional(readOnly = true)
    public List<StateDto> getStatesByCountryId(Integer countryId) {
        return stateRepository.findByCountryIdWithCountry(countryId)
                .stream()
                .map(mappingService::toStateDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StateDto> searchStatesByName(String name) {
        return stateRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(mappingService::toStateDto)
                .collect(Collectors.toList());
    }

    public StateDto createState(StateDto stateDto) {
        Country country = countryRepository.findById(stateDto.getCountryId())
                .orElseThrow(() -> new IllegalArgumentException("Country not found with id: " + stateDto.getCountryId()));

        if (stateRepository.existsByNameAndCountryId(stateDto.getName(), stateDto.getCountryId())) {
            throw new IllegalArgumentException("State with name '" + stateDto.getName() +
                    "' already exists in country '" + country.getName() + "'");
        }

        State state = mappingService.toStateEntity(stateDto, country);
        State savedState = stateRepository.save(state);
        return mappingService.toStateDto(savedState);
    }

    public StateDto updateState(Integer id, StateDto stateDto) {
        State existingState = stateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("State not found with id: " + id));

        Country country = countryRepository.findById(stateDto.getCountryId())
                .orElseThrow(() -> new IllegalArgumentException("Country not found with id: " + stateDto.getCountryId()));

        if (stateRepository.existsByNameAndCountryId(stateDto.getName(), stateDto.getCountryId()) &&
                (!existingState.getName().equals(stateDto.getName()) || !existingState.getCountry().getId().equals(stateDto.getCountryId()))) {
            throw new IllegalArgumentException("State with name '" + stateDto.getName() +
                    "' already exists in country '" + country.getName() + "'");
        }

        existingState.setName(stateDto.getName());
        existingState.setCountry(country);
        State updatedState = stateRepository.save(existingState);
        return mappingService.toStateDto(updatedState);
    }

    public void deleteState(Integer id) {
        if (!stateRepository.existsById(id)) {
            throw new IllegalArgumentException("State not found with id: " + id);
        }
        stateRepository.deleteById(id);
    }


}
