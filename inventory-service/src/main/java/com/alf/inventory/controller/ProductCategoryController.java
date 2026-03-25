package com.alf.inventory.controller;

import com.alf.inventory.entity.ProductCategory;
import com.alf.inventory.services.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product-categories")
@RequiredArgsConstructor
public class ProductCategoryController {

    private final ProductCategoryService service;

    @PostMapping
    public ProductCategory create(@RequestBody ProductCategory c){
        return service.create(c);
    }

    @GetMapping
    public List<ProductCategory> list(){
        return service.list();
    }

}
