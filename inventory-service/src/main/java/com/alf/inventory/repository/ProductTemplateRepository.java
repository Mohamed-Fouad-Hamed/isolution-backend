package com.alf.inventory.repository;

import com.alf.inventory.entity.ProductTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTemplateRepository
        extends JpaRepository<ProductTemplate, Long> {
}

