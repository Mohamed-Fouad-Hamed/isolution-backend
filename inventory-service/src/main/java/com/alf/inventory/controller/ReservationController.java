package com.alf.inventory.controller;

import com.alf.inventory.dto.ReservationRequest;
import com.alf.inventory.services.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService service;

    @PostMapping
    public void reserve(@RequestBody ReservationRequest request) {
        service.reserve(request);
    }
}
