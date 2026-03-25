package com.alf.inventory.repository;

import com.alf.inventory.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository
        extends JpaRepository<ProductCategory, Long> {
}

