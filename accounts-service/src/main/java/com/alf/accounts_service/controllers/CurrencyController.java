package com.alf.accounts_service.controllers;

import com.alf.accounts_service.dtos.currency.CurrencyDto;
import com.alf.accounts_service.services.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/currencies")
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping
    public ResponseEntity<List<CurrencyDto>> getAllCurrencies(){
        List<CurrencyDto> currencies = currencyService.getAllCurrencies();
        return ResponseEntity.ok(currencies);
    }


}

