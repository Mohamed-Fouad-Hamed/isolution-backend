package com.alf.accounts_service.repositories;


import com.alf.accounts_service.models.AccountPayment;
import com.alf.accounts_service.models.AccountPaymentKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountPaymentRepository extends JpaRepository<AccountPayment, AccountPaymentKey> {
}
