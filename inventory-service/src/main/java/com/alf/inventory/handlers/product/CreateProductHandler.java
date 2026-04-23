package com.alf.inventory.handlers.product;

import com.alf.inventory.dto.CreateProductCommand;
import com.alf.inventory.dto.ProductDTO;
import com.alf.inventory.entity.Product;
import com.alf.inventory.entity.ProductTemplate;
import com.alf.inventory.entity.Uom;
import com.alf.inventory.mapper.ProductMapper;
import com.alf.inventory.repository.ProductRepository;
import com.alf.inventory.repository.ProductTemplateRepository;
import com.alf.inventory.repository.UomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CreateProductHandler {
    private final ProductRepository productRepository;
    private final ProductTemplateRepository templateRepository;
    private final UomRepository uomRepository;
    private final ProductMapper productMapper;

    @Transactional
    public ProductDTO create(CreateProductCommand cmd) {

        ProductTemplate template =
                templateRepository.getReferenceById(cmd.templateId());

        Uom uom =
                uomRepository.getReferenceById(cmd.uomId());

        Product product = productMapper.toEntity(cmd);

        product = productRepository.save(product);

        return productMapper.toDto(product);
    }
}
