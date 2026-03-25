package com.alf.accounts_service.dtos.account;

import java.util.List;

public record AccountOptionReq(
        Long id,
         float min_value,
         float min_quan,
         Integer currency_id,

         float credit,

         float rating,
         String delivery_period,
         String weekend,
         String work_hours ,

         List<AccountPaymentReq> paymentTypes
) {
}
