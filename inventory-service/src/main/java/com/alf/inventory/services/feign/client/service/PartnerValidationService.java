package com.alf.inventory.services.feign.client.service;

import com.alf.inventory.services.feign.client.interfaces.PartnerClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartnerValidationService {
    private final PartnerClient partnerClient;

    @CircuitBreaker(name = "partnerService", fallbackMethod = "fallback")
    public void validatePartner(Long partnerId) {
        Boolean exists = partnerClient.checkPartnerExists(partnerId);

        if (exists == null || !exists) {
            throw new EntityNotFoundException("Partner not found");
        }
    }

    public void fallback(Long partnerId, Throwable ex) {
        throw new RuntimeException("Partner service unavailable");
    }


}
