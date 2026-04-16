package com.alf.inventory.services;

import com.alf.inventory.entity.StockMove;
import com.alf.inventory.repository.StockMoveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockMoveService {

    private final StockMoveRepository repository;

    public StockMove create(StockMove move){
        return repository.save(move);
    }



}
