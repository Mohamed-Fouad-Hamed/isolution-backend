package com.alf.inventory.controller;

import com.alf.inventory.entity.StockMove;
import com.alf.inventory.services.StockMoveService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stock/moves")
@RequiredArgsConstructor
public class StockMoveController {

    private final StockMoveService service;

    @PostMapping
    public StockMove create(@RequestBody StockMove move){
        return service.create(move);
    }

}
