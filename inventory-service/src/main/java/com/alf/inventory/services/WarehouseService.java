package com.alf.inventory.services;

import com.alf.inventory.entity.Warehouse;
import com.alf.inventory.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository repository;

    public Warehouse create(Warehouse w){
        return repository.save(w);
    }

    public List<Warehouse> list(){
        return repository.findAll();
    }

}

