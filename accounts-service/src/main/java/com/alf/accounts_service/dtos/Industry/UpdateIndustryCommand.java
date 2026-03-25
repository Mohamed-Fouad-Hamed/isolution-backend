package com.alf.accounts_service.dtos.Industry;

public record UpdateIndustryCommand (
                                         String name,
                                         String description,
                                         boolean active
) {

}
