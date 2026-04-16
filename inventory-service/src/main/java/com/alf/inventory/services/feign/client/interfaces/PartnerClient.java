package com.alf.inventory.services.feign.client.interfaces;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "accounts-service", url = "${services.accounts.url}")
public interface PartnerClient {
    @GetMapping("/api/v1/accounts/{id}/exists")
    Boolean checkPartnerExists(@PathVariable("id") Long id);
}
