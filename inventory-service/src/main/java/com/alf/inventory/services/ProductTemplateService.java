package com.alf.inventory.services;

import com.alf.inventory.entity.ProductTemplate;
import com.alf.inventory.repository.ProductTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductTemplateService {

    private final ProductTemplateRepository repository;

    public ProductTemplate create(ProductTemplate template){
        return repository.save(template);
    }

    public List<ProductTemplate> list(){
        return repository.findAll();
    }

}

