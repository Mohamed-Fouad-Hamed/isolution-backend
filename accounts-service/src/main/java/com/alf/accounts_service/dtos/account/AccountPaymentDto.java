package com.alf.accounts_service.dtos.account;

public record AccountPaymentDto(
          Integer accountId ,
          Integer paymentId ,
          String payment_Name ,
          String wallet_number ,
          String wallet_password ,
          String card_holder_name ,
          String account_number ,
          String expiry_month ,
          String expiry_year ,
          String cvc
) {
}
