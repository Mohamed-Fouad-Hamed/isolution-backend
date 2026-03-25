package com.alf.inventory.mapper;

import com.alf.inventory.dto.CreateProductTemplateCommand;
import com.alf.inventory.dto.ProductTemplateResponse;
import com.alf.inventory.entity.ProductTemplate;
import com.alf.inventory.entity.Uom;
import org.springframework.stereotype.Component;

@Component
public class ProductTemplateMapper {

    public ProductTemplate toEntity(CreateProductTemplateCommand cmd,
                                    Uom uom,
                                    Uom purchaseUom) {

        ProductTemplate entity = new ProductTemplate();

        entity.setName(cmd.name());
        entity.setProductType(cmd.productType());
        entity.setUom(uom);
        entity.setPurchaseUom(purchaseUom);
        entity.setWeight(cmd.weight());
        entity.setVolume(cmd.volume());

        return entity;
    }

    public ProductTemplateResponse toResponse(ProductTemplate entity) {

        return new ProductTemplateResponse(
                entity.getId(),
                entity.getName(),
                entity.getProductType(),
                entity.getUom().getId(),
                entity.getPurchaseUom() != null ? entity.getPurchaseUom().getId() : null,
                entity.getWeight(),
                entity.getVolume(),
                entity.getActive()
        );
    }

}
