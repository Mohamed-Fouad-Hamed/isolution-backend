package com.alf.accounts_service.dtos.payment;

import java.util.List;

public record PaymentTermDto(
        Long id,
        String code,
        String name,
        Boolean active,
        List<PaymentTermLineDTO> lines
) {
}
