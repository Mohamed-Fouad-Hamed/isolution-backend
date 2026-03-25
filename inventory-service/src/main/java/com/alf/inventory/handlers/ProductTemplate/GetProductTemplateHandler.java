package com.alf.inventory.handlers.ProductTemplate;

import com.alf.inventory.dto.ProductTemplateResponse;
import com.alf.inventory.entity.ProductTemplate;
import com.alf.inventory.mapper.ProductTemplateMapper;
import com.alf.inventory.repository.ProductTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetProductTemplateHandler {

    private final ProductTemplateRepository repository;
    private final ProductTemplateMapper mapper;

    public ProductTemplateResponse handle(Long id) {

        ProductTemplate entity = repository.findById(id)
                .orElseThrow();

        return mapper.toResponse(entity);
    }
}

