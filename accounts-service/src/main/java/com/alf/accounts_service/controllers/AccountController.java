package com.alf.accounts_service.controllers;


import com.alf.accounts_service.dtos.account.AccountCompany;
import com.alf.accounts_service.dtos.account.AccountDto;
import com.alf.accounts_service.services.AccountService;
import com.alf.core_common.dtos.payload.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    private static final Logger log = LoggerFactory.getLogger(AccountController.class);

    @GetMapping("/account-id")
    public ResponseEntity<AccountDto> getAccount(@RequestParam String id){
        Long accountId = Long.valueOf(id);
        AccountDto result = accountService.getAccount(accountId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/account-list")
    public ResponseEntity<List<AccountDto>> getAccounts(){
        List<AccountDto> result = accountService.getAccounts();
        return ResponseEntity.ok(result);
    }


    // --- DELETE ---
    @DeleteMapping("/{serialId}")
    public ResponseEntity<Boolean> deleteAccount(
            @PathVariable String serialId) {
        log.info("DELETE /api/v1/accounts/accounts/{}", serialId);
        boolean result =  accountService.deleteAccount(serialId);
        return ResponseEntity.ok(result);
    }


    @PostMapping("/company-register")
    public ResponseEntity<MessageResponse> accountRegister(@RequestBody AccountCompany accountCompany){

        int status = 200;

        String message = "ok";

        Map<String,Long> result = new HashMap<>();

        if( accountCompany.accountName() == "" || accountCompany.accountName() == null)
            return  ResponseEntity.ok(
                    new MessageResponse("Name is not found",703,703,null,null)
            );

        try {

            result = accountService.updateAccount(accountCompany);

        } catch (RuntimeException ex) {

            status = 400 ;

            message = ex.getMessage();
        }

        return ResponseEntity.ok( new MessageResponse(message,status,status,result,null)  );
    }



}
