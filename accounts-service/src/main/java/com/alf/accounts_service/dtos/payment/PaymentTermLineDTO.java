package com.alf.accounts_service.dtos.payment;

import com.alf.accounts_service.models.enums.DueType;
import com.alf.accounts_service.models.enums.ValueType;

import java.math.BigDecimal;

public record PaymentTermLineDTO(
        Long id,
        ValueType valueType,
        BigDecimal valueAmount,
        DueType dueType,
        Integer days,
        Integer sequence
) {
}
