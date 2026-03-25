package com.alf.accounts_service.controllers;

import com.alf.accounts_service.dtos.country.CountryDto;
import com.alf.accounts_service.dtos.country.CountryStateJoinDto;
import com.alf.accounts_service.dtos.base.ItemReq;
import com.alf.accounts_service.services.CountryService;
import com.alf.core_common.dtos.payload.MessagePageableResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/countries")
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;

    @GetMapping
    public ResponseEntity<List<CountryDto>> getAllCountries() {
        List<CountryDto> countries = countryService.getAllCountries();
        return ResponseEntity.ok(countries);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CountryDto> getCountryById(@PathVariable Integer id) {
        return countryService.getCountryById(id)
                .map(country -> ResponseEntity.ok(country))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<CountryDto>> searchCountries(@RequestParam("q") String q) {
        List<CountryDto> countries = countryService.searchCountriesByName(q);
        return ResponseEntity.ok(countries);
    }


    @GetMapping("/paged")
    public ResponseEntity<MessagePageableResponse> searchCountriesPaged(
            @RequestParam String q
            , @RequestParam Integer page
            , @RequestParam Integer limit
    ) {
        List<CountryDto> countries = null;

        int status = 200;

        String message = "ok";

        Long count = 0L;

        try {

            count = countryService.countCountriesByName(q);

            countries = countryService.searchCountriesByName(q,page,limit);

        } catch (RuntimeException ex) {

            status = 400 ;

            message = ex.getMessage();
        }

        return ResponseEntity.ok( new MessagePageableResponse(message,count,status,countries ));
    }

    @GetMapping("/with-states")
    public ResponseEntity<List<CountryStateJoinDto>> getAllCountriesWithStates() {
        List<CountryStateJoinDto> result = countryService.getAllCountriesWithStates();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}/states-count")
    public ResponseEntity<Long> getStateCount(@PathVariable Integer id) {
        Long count = countryService.getStateCountByCountryId(id);
        return ResponseEntity.ok(count);
    }

    @PostMapping
    public ResponseEntity<CountryDto> createCountry( @RequestBody CountryDto countryDto) {
        try {
            CountryDto createdCountry = countryService.createCountry(countryDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCountry);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/add-shorthand")
    public ResponseEntity<CountryDto> createCountryShorthand( @RequestBody ItemReq itemReq) {
        try {
            CountryDto createdCountry = countryService.createCountryShorthand(itemReq);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCountry);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CountryDto> updateCountry(@PathVariable Integer id,  @RequestBody CountryDto countryDto) {
        try {
            CountryDto updatedCountry = countryService.updateCountry(id, countryDto);
            return ResponseEntity.ok(updatedCountry);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable Integer id) {
        try {
            countryService.deleteCountry(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
