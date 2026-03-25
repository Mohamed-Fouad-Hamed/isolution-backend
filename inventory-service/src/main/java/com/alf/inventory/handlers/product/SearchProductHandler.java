package com.alf.inventory.handlers.product;

import com.alf.inventory.dto.ProductDTO;
import com.alf.inventory.mapper.ProductMapper;
import com.alf.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class SearchProductHandler {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> list(Pageable pageable){

        return productRepository
                .findAll(pageable)
                .map(ProductMapper::toDto);

    }
}
