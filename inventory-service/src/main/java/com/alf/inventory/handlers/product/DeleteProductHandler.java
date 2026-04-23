package com.alf.inventory.handlers.product;

import com.alf.inventory.entity.Product;
import com.alf.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteProductHandler {
    private final ProductRepository productRepository;
    // 🔹 Delete (Soft delete)
    public void delete(Long id) {
        Product entity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        entity.setActive(false);
        productRepository.save(entity);
    }

}
