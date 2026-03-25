package com.alf.accounts_service.services;


import com.alf.accounts_service.dtos.country.CountryDto;
import com.alf.accounts_service.dtos.country.CountryStateJoinDto;
import com.alf.accounts_service.dtos.base.ItemReq;
import com.alf.accounts_service.mappers.CountryMappingService;
import com.alf.accounts_service.models.Country;
import com.alf.accounts_service.repositories.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CountryService {
    private final CountryRepository countryRepository;
    private final CountryMappingService mappingService;

    @Transactional(readOnly = true)
    public List<CountryDto> getAllCountries() {
        return countryRepository.findAll()
                .stream()
                .map(mappingService::toCountryDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<CountryDto> getCountryById(Integer id) {
        return countryRepository.findByIdWithStates(id)
                .map(mappingService::toCountryDtoWithStates);
    }

    @Transactional(readOnly = true)
    public List<CountryDto> searchCountriesByName(String name) {
        return countryRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(mappingService::toCountryDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CountryDto> searchCountriesByName(String name  , Integer pageNumber , Integer pageSize ) {
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        return countryRepository.findByNameContainingIgnoreCase(name , pageable)
                .stream()
                .map(mappingService::toCountryDto)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public Long countCountriesByName(String name) {
        return countryRepository.countByNameContainingIgnoreCase(name);
    }

    @Transactional(readOnly = true)
    public List<CountryStateJoinDto> getAllCountriesWithStates() {
        return countryRepository.findAllCountriesWithStates();
    }

    public CountryDto createCountry(CountryDto countryDto) {
        if (countryRepository.findByName(countryDto.getName()).isPresent()) {
            throw new IllegalArgumentException("Country with name '" + countryDto.getName() + "' already exists");
        }

        Country country = mappingService.toCountryEntity(countryDto);
        Country savedCountry = countryRepository.save(country);
        return mappingService.toCountryDto(savedCountry);
    }

    public CountryDto createCountryShorthand(ItemReq itemReq) {
        if (countryRepository.findByName(itemReq.name()).isPresent()) {
            throw new IllegalArgumentException("Country with name '" + itemReq.name() + "' already exists");
        }

        Country country = new Country(null,itemReq.name(),null);
        Country savedCountry = countryRepository.save(country);
        return mappingService.toCountryDto(savedCountry);
    }

    public CountryDto updateCountry(Integer id, CountryDto countryDto) {
        Country existingCountry = countryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Country not found with id: " + id));

        Optional<Country> duplicateCountry = countryRepository.findByName(countryDto.getName());
        if (duplicateCountry.isPresent() && !duplicateCountry.get().getId().equals(id)) {
            throw new IllegalArgumentException("Country with name '" + countryDto.getName() + "' already exists");
        }

        existingCountry.setName(countryDto.getName());
        Country updatedCountry = countryRepository.save(existingCountry);
        return mappingService.toCountryDto(updatedCountry);
    }

    public void deleteCountry(Integer id) {
        if (!countryRepository.existsById(id)) {
            throw new IllegalArgumentException("Country not found with id: " + id);
        }
        countryRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Long getStateCountByCountryId(Integer countryId) {
        return countryRepository.countStatesByCountryId(countryId);
    }
}
