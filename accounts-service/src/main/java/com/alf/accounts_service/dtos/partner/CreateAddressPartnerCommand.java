package com.alf.accounts_service.dtos.partner;

import com.alf.accounts_service.models.enums.PartnerType;

public record CreateAddressPartnerCommand(
        String name,
        PartnerType type,   // INVOICE / DELIVERY

        Long parentId,
        Long commercialPartnerId,

        String street,
        String city,
        String country,
        String phone
) {
}
