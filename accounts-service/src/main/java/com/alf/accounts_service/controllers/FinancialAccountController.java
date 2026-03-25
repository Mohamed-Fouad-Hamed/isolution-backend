package com.alf.accounts_service.controllers;

import com.alf.accounts_service.services.FinancialAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/financial-accounts")
@RequiredArgsConstructor
public class FinancialAccountController {
    private final FinancialAccountService financialAccountService;

}
