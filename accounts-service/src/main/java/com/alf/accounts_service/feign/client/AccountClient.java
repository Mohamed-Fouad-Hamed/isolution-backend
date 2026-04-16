package com.alf.accounts_service.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "accounts-service", url = "${services.accounts.url}")
public interface AccountClient {
    @GetMapping("/api/v1/accounts/{id}/exists")
    Boolean checkAccountExists(@PathVariable("id") Long id);
}
