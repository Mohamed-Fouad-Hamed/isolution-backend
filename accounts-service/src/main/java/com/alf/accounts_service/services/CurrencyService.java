package com.alf.accounts_service.services;


import com.alf.accounts_service.dtos.currency.CurrencyDto;
import com.alf.accounts_service.mappers.CurrencyMappingService;
import com.alf.accounts_service.repositories.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    private final CurrencyMappingService currencyMappingService;

    @Transactional(readOnly = true)
    public List<CurrencyDto> getAllCurrencies(){
        return currencyRepository
                .findAll()
                .stream()
                .map(currencyMappingService::toCurrencyDto)
                .collect(Collectors.toList());
    }

}
