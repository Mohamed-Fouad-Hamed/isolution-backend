package com.alf.inventory.handlers.ProductTemplate;

import com.alf.inventory.dto.CreateProductTemplateCommand;
import com.alf.inventory.entity.ProductTemplate;
import com.alf.inventory.entity.Uom;
import com.alf.inventory.mapper.ProductTemplateMapper;
import com.alf.inventory.repository.ProductTemplateRepository;
import com.alf.inventory.repository.UomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateProductTemplateHandler {

    private final ProductTemplateRepository repository;
    private final UomRepository uomRepository;
    private final ProductTemplateMapper mapper;

    public Long handle(CreateProductTemplateCommand cmd) {

        Uom uom = uomRepository.getReferenceById(cmd.uomId());

        Uom purchaseUom = null;

        if (cmd.purchaseUomId() != null) {
            purchaseUom = uomRepository.getReferenceById(cmd.purchaseUomId());
        }

        ProductTemplate entity =
                mapper.toEntity(cmd, uom, purchaseUom);

        repository.save(entity);

        return entity.getId();
    }
}

