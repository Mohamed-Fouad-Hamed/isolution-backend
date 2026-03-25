package com.alf.accounts_service.dtos.payment;

import java.util.List;

public record CreatePaymentTermCommand(
        String code,
        String name,
        String description,
        List<CreatePaymentTermLineCommand> lines
) {
}
