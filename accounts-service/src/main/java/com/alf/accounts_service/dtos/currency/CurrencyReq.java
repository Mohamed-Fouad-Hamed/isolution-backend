package com.alf.accounts_service.dtos.currency;

public record CurrencyReq(

        String code ,

        String name ,

        String symbol ,

        boolean active
) {
}