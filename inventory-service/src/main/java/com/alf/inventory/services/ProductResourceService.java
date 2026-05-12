package com.alf.inventory.services;

import com.alf.inventory.entity.Product;
import com.alf.inventory.entity.ProductResourceMapping;
import com.alf.inventory.entity.Resource;
import com.alf.inventory.enums.ResourceType;
import com.alf.inventory.repository.ProductRepository;
import com.alf.inventory.repository.ProductResourceRepository;
import com.alf.inventory.repository.ResourceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductResourceService {


    private final ProductRepository productRepository;


    private final ResourceRepository resourceRepository;


    private final ProductResourceRepository mappingRepository;

    @Transactional
    public void addResourceToProduct(Long productId, Long resourceId, ResourceType type) {
        // 1. جلب المنتج (Proxy أو كامل حسب الحاجة)
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        // 2. جلب المورد الفيزيائي
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new EntityNotFoundException("Resource not found"));

        // 3. إنشاء كائن الربط وتحديد العلاقة والنوع
        ProductResourceMapping mapping = new ProductResourceMapping();
        mapping.setProduct(product);
        mapping.setResource(resource);
        mapping.setResourceType(type);

        // 4. حفظ في جدول الربط
        mappingRepository.save(mapping);
    }

    @Transactional
    public void addResourceFast(Long productId, Long resourceId, ResourceType type) {
        // إنشاء مراجع (Proxies) بدون استعلام SELECT
        Product productProxy = productRepository.getReferenceById(productId);
        Resource resourceProxy = resourceRepository.getReferenceById(resourceId);

        ProductResourceMapping mapping = new ProductResourceMapping();
        mapping.setProduct(productProxy);
        mapping.setResource(resourceProxy);
        mapping.setResourceType(type);

        mappingRepository.save(mapping);
        // هنا Hibernate سيقوم بعمل INSERT واحد فقط في جدول product_resource_mapping
    }

}

