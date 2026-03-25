package com.alf.accounts_service.dtos.account;

public record AccountCompany(

        String tempId,
        String serialId,
        String ownerId,
        String accountName,

        String imageUrl,
        String taxIdentificationNumber,
        String commercialRegistry,
        String address,
        String phone,
        String email,
        String website,

        Long partnerId,
        Long parentId,
        Integer currencyId,
        Integer stateId,
        Integer countryId,
        String color_account,

        AccountCompany[] branches
) {

}
