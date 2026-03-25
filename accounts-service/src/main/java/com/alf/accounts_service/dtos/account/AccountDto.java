package com.alf.accounts_service.dtos.account;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDto{
    public AccountDto( Long id,String serialId, String ownerId, String accountName, String imageUrl, String taxIdentificationNumber, String commercialRegistry, String address, String phone, String email, String website, Long partnerId, Long parentId, Integer currencyId, Integer stateId, Integer countryId, String color_account) {
        this.id = id;
        this.serialId = serialId;
        this.ownerId = ownerId;
        this.accountName = accountName;
        this.imageUrl = imageUrl;
        this.taxIdentificationNumber = taxIdentificationNumber;
        this.commercialRegistry = commercialRegistry;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.website = website;
        this.partnerId = partnerId;
        this.parentId = parentId;
        this.currencyId = currencyId;
        this.stateId = stateId;
        this.countryId = countryId;
        this.color_account = color_account;
    }

    private Long id;
    private   String serialId;
    private   String ownerId;
    private   String accountName;
    private  String imageUrl;
    private  String taxIdentificationNumber;
    private String commercialRegistry;
    private String address;
    private   String phone;
    private  String email;
    private   String website;
    private   Long partnerId;
    private   Long parentId;
    private   Integer currencyId;
    private   Integer stateId;
    private   Integer countryId;
    private   String color_account ;
}
