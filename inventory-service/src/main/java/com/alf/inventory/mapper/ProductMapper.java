package com.alf.inventory.mapper;

import com.alf.inventory.dto.CreateProductCommand;
import com.alf.inventory.dto.ProductDTO;
import com.alf.inventory.dto.ProductVariantRequest;
import com.alf.inventory.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(CreateProductCommand request){
        Product product = new Product();
        product.setTemplateId(request.templateId());
        product.setSku(request.sku());
        product.setBarcode(request.barcode());
        product.setActive(request.active());
        return product;
    }

    public ProductDTO toDto(Product product){
        return new ProductDTO(product.getId(),product.getSku(),product.getTemplateId(),product.getActive());
    }
}

