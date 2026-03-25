package com.alf.inventory.controller;

import com.alf.inventory.entity.Uom;
import com.alf.inventory.services.UomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/uoms")
@RequiredArgsConstructor
public class UomController {

    private final UomService service;

    @PostMapping
    public Uom create(@RequestBody Uom uom){
        return service.create(uom);
    }

    @GetMapping
    public List<Uom> list(){
        return service.list();
    }

}

