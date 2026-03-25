package com.alf.accounts_service.repositories;

import com.alf.accounts_service.models.PaymentTermLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentTermLineRepository
        extends JpaRepository<PaymentTermLine, Long> {
}
