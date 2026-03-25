package com.alf.accounts_service.repositories;

import com.alf.accounts_service.models.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency,Integer> {
}
