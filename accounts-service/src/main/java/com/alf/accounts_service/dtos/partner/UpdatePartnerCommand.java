package com.alf.accounts_service.dtos.partner;

public record UpdatePartnerCommand(
                                   String name,
                                   String displayName,

                                   Boolean active,

                                   boolean customer,
                                   boolean supplier,
                                   boolean employee
) {
}
