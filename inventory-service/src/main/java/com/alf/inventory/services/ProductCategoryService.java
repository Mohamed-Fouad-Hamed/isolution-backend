package com.alf.inventory.services;

import com.alf.inventory.entity.ProductCategory;
import com.alf.inventory.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {

    private final ProductCategoryRepository repository;

    public ProductCategory create(ProductCategory category){
        return repository.save(category);
    }

    public List<ProductCategory> list(){
        return repository.findAll();
    }

}

