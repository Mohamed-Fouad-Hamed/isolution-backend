package com.alf.accounts_service.dtos.partner;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PartnerLookupDto {
   private Long id ;
   private String name ;
   private String displayName ;
}

