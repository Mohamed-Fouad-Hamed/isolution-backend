package com.alf.inventory.mapper;

import com.alf.inventory.dto.CreateProductCommand;
import com.alf.inventory.dto.ProductDTO;
import com.alf.inventory.entity.Product;
import com.alf.inventory.entity.ProductTemplate;
import com.alf.inventory.entity.Uom;

public class ProductMapper {

    public static ProductDTO toDto(Product p){

        return new ProductDTO(
                p.getId(),
                p.getTemplate().getName(),
                p.getSku(),
                p.getTemplate().getId(),
                p.getActive()
        );

    }

    public static Product toEntity(CreateProductCommand cmd,
                                   ProductTemplate template,
                                   Uom uom) {

        Product product = new Product();

        product.setSku(cmd.sku());
        product.setBarcode(cmd.barcode());
        product.setTemplate(template);
        product.setUom(uom);

        return product;
    }
}
