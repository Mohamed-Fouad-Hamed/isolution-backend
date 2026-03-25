package com.alf.inventory.services;

import com.alf.inventory.entity.Uom;
import com.alf.inventory.repository.UomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UomService {

    private final UomRepository repository;

    public Uom create(Uom uom){
        return repository.save(uom);
    }

    public List<Uom> list(){
        return repository.findAll();
    }

}

