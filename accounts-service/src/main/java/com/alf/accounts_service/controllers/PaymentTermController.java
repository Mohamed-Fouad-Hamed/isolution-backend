package com.alf.accounts_service.controllers;

import com.alf.accounts_service.dtos.payment.CreatePaymentTermCommand;
import com.alf.accounts_service.dtos.type.CursorPageTypeResponse;
import com.alf.accounts_service.dtos.type.TypeCursor;
import com.alf.accounts_service.dtos.type.TypeLookupDto;
import com.alf.accounts_service.models.PaymentTerm;
import com.alf.accounts_service.services.PaymentTermService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment-terms")
@RequiredArgsConstructor
public class PaymentTermController {

    private final PaymentTermService service;


    @PostMapping
    public PaymentTerm create(
            @RequestBody CreatePaymentTermCommand cmd
    ){
        return service.create(cmd);
    }

    @GetMapping
    public List<PaymentTerm> list(){
        return service.list();
    }

    @GetMapping("/{id}")
    public PaymentTerm get(@PathVariable Long id){
        return service.get(id);
    }

    @GetMapping("/search")
    public CursorPageTypeResponse<TypeLookupDto> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "10") int size
    ) {

        TypeCursor cursor =
                (lastName == null && lastId == null)
                        ? null
                        : new TypeCursor(lastName, lastId);

        return service.search(keyword, cursor, size);
    }



}

