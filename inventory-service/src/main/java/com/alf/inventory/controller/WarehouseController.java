package com.alf.inventory.controller;

import com.alf.inventory.entity.Warehouse;
import com.alf.inventory.services.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService service;

    @PostMapping
    public Warehouse create(@RequestBody Warehouse w){
        return service.create(w);
    }

    @GetMapping
    public List<Warehouse> list(){
        return service.list();
    }

}

