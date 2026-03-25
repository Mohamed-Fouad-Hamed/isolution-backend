package com.alf.accounts_service.repositories;

import com.alf.accounts_service.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Integer> {
}
