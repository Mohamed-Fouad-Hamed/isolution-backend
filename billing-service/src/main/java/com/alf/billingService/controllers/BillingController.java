package com.alf.billingService.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillingController {
    private static final Logger log = LoggerFactory.getLogger(BillingController.class);

    @GetMapping("/ping")
    public String ping() {
        log.info("📡 Received /ping request");
        return "OH...!, Billing Service via Gateway ✅";
    }
}
