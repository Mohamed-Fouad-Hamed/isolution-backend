package com.alf.inventory.services;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    public List<Long> findReservableLocations(Long warehouseId) {

        return List.of(1L, 2L, 3L);
    }
}

