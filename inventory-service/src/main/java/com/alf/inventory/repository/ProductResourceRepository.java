package com.alf.inventory.repository;

import com.alf.inventory.entity.ProductResourceMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductResourceRepository extends JpaRepository<ProductResourceMapping, Long> {

    // استعلام سريع جداً يجلب فقط الروابط والأنواع لمنتج معين
    @Query("SELECT m.resource.url, m.resourceType FROM ProductResourceMapping m " +
            "WHERE m.product.id = :productId")
    List<Object[]> findResourceUrlsByProductId(@Param("productId") Long productId);
}

