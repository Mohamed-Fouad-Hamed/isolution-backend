package com.alf.inventory.handlers.product;

import com.alf.inventory.dto.ProductDTO;
import com.alf.inventory.dto.UpdateProductCommand;
import com.alf.inventory.entity.Product;
import com.alf.inventory.mapper.ProductMapper;
import com.alf.inventory.repository.ProductRepository;
import com.alf.inventory.repository.ProductTemplateRepository;
import com.alf.inventory.repository.UomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateProductHandler {
    private final ProductRepository productRepository;
    private final ProductTemplateRepository templateRepository;
    private final UomRepository uomRepository;
    private final ProductMapper productMapper;

    @Transactional
    public ProductDTO update(Long id, UpdateProductCommand cmd) {

        Product product = productRepository.findById(id)
                .orElseThrow();

        product.setSku(cmd.sku());
        product.setBarcode(cmd.barcode());

        if(cmd.templateId() != null){
            product.setTemplateId(
                    cmd.templateId()
            );
        }

        return productMapper.toDto(product);
    }
}
