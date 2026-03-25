package com.alf.inventory.services;

import com.alf.inventory.dto.CreateProductCommand;
import com.alf.inventory.dto.ProductDTO;
import com.alf.inventory.dto.UpdateProductCommand;
import com.alf.inventory.entity.Product;
import com.alf.inventory.entity.ProductTemplate;
import com.alf.inventory.entity.Uom;
import com.alf.inventory.mapper.ProductMapper;
import com.alf.inventory.repository.ProductRepository;
import com.alf.inventory.repository.ProductTemplateRepository;
import com.alf.inventory.repository.UomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductTemplateRepository templateRepository;
    private final UomRepository uomRepository;
    private final ProductMapper mapper;

    public Product create(CreateProductCommand cmd) {

        ProductTemplate template =
                templateRepository.getReferenceById(cmd.templateId());

        Uom uom =
                uomRepository.getReferenceById(cmd.uomId());

        Product product = mapper.toEntity(cmd, template, uom);

        return productRepository.save(product);
    }




    @Transactional(readOnly = true)
    public List<Product> list(){
        return productRepository.findAll();
    }




}

