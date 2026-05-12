package com.alf.inventory.services;

import com.alf.inventory.dto.MediaResourceDTO;
import com.alf.inventory.dto.ProductDetailsResponse;
import com.alf.inventory.dto.ProductWithMainImageDTO;
import com.alf.inventory.entity.Product;
import com.alf.inventory.enums.ResourceType;
import com.alf.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public ProductWithMainImageDTO getProductWithMainImage(Long productId) {
        return productRepository.findProductWithMainImage(productId, ResourceType.IMAGE)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    /*
    @Transactional(readOnly = true)
    public ProductDetailsResponse getProductWithMedia(Long id) {
        Product product = productRepository.findWithResourcesById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // تحويل الكيانات (Entities) إلى DTOs بسيطة
        List<MediaResourceDTO> mediaList = product.getResourceMappings().stream()
                .map(mapping -> new MediaResourceDTO(
                        mapping.getResource().getUrl(),
                        mapping.getResourceType().name(),
                        mapping.getResource().getId()
                ))
                .collect(Collectors.toList());

        return new ProductDetailsResponse(product.getId(), product.getName(), product.getPrice(), mediaList);
    }
*/
}
