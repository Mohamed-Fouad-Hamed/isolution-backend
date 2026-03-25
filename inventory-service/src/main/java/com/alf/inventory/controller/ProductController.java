package com.alf.inventory.controller;

import com.alf.inventory.dto.CreateProductCommand;
import com.alf.inventory.dto.ProductDTO;
import com.alf.inventory.dto.UpdateProductCommand;
import com.alf.inventory.handlers.product.CreateProductHandler;
import com.alf.inventory.handlers.product.SearchProductHandler;
import com.alf.inventory.handlers.product.UpdateProductHandler;
import com.alf.inventory.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final CreateProductHandler createProductHandler;

    private final UpdateProductHandler updateProductHandler;

    private final SearchProductHandler searchProductHandler;

    @GetMapping
    public Page<ProductDTO> list(Pageable pageable){
        return searchProductHandler.list(pageable);
    }

    @PostMapping
    public ProductDTO create(
             @RequestBody CreateProductCommand cmd){
        return createProductHandler.create(cmd);
    }

    @PutMapping("/{id}")
    public ProductDTO update(  @PathVariable Long id,
            @RequestBody UpdateProductCommand cmd
    ){
        return updateProductHandler.update(id , cmd);
    }


}

