package com.alf.accounts_service.mappers;


import com.alf.accounts_service.dtos.currency.CurrencyDto;
import com.alf.accounts_service.dtos.currency.CurrencyReq;
import com.alf.accounts_service.models.Currency;
import org.springframework.stereotype.Service;

@Service
public class CurrencyMappingService {

    public CurrencyDto toCurrencyDto(Currency currency){

        CurrencyDto currencyDto = new CurrencyDto();
        currencyDto.setId(currency.getId());
        currencyDto.setSerialId(currency.getSerialId());
        currencyDto.setName(currency.getName());
        currencyDto.setCode(currency.getCode());
        currencyDto.setSymbol(currency.getSymbol());
        currencyDto.setActive(currency.isActive());

        return  currencyDto;

    }

    public Currency fromCurrencyReq(CurrencyReq currencyReq){
        Currency currency = new Currency();
        currency.setName(currencyReq.name());
        currency.setCode(currencyReq.code());
        currency.setSymbol(currencyReq.symbol());
        currency.setActive(currencyReq.active());
        return  currency;
    }
}
