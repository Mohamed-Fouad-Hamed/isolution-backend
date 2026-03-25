package com.alf.inventory.controller;

import com.alf.inventory.dto.CreateProductTemplateCommand;
import com.alf.inventory.dto.ProductTemplateResponse;
import com.alf.inventory.entity.ProductTemplate;
import com.alf.inventory.handlers.ProductTemplate.CreateProductTemplateHandler;
import com.alf.inventory.handlers.ProductTemplate.GetProductTemplateHandler;
import com.alf.inventory.services.ProductTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products/templates")
@RequiredArgsConstructor
public class ProductTemplateController {
    private final CreateProductTemplateHandler createHandler;
    private final GetProductTemplateHandler getHandler;

    @PostMapping
    public ResponseEntity<Long> create(
            @RequestBody CreateProductTemplateCommand cmd) {

        Long id = createHandler.handle(cmd);

        return ResponseEntity.ok(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductTemplateResponse> get(
            @PathVariable Long id) {

        return ResponseEntity.ok(getHandler.handle(id));
    }

}

