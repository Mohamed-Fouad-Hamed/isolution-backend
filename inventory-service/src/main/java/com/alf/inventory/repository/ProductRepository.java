package com.alf.inventory.repository;

import com.alf.inventory.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.alf.inventory.dto.ProductVariantResponse;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository
        extends JpaRepository<Product, Long> {

    Optional<Product> findBySku(String sku);

    Optional<Product> findByBarcode(String barcode);

    boolean existsBySku(String sku);

    @Query("""
    SELECT new com.alf.inventory.dto.ProductVariantResponse(
          v.id,
          v.sku,
          t.name,
          u.name,
          v.barcode,
          v.active
    )
    FROM Product v
    JOIN ProductTemplate t ON v.templateId = t.id
    JOIN Uom u ON t.saleUomId = u.id
    """)
    List<ProductVariantResponse> findVariantAll();


    @Query("""
    SELECT new com.alf.inventory.dto.ProductVariantResponse(
        v.id,
        v.sku,
        t.name,
        u.name,
        v.barcode,
        v.active
    )
    FROM Product v
    JOIN ProductTemplate t ON v.templateId = t.id
    JOIN Uom u ON t.saleUomId = u.id
    """)
    Page<ProductVariantResponse> findAllDetailed(Pageable pageable);


    @Query("""
    SELECT new com.alf.inventory.dto.ProductVariantResponse(
        v.id,
        v.sku,
        t.name,
        u.name,
        v.barcode,
        v.active
    )
    FROM Product v
    JOIN ProductTemplate t ON v.templateId = t.id
    JOIN Uom u ON t.saleUomId = u.id
    WHERE (
        :search IS NULL OR
        LOWER(v.sku) LIKE LOWER(CONCAT(:search, '%')) OR
        LOWER(t.name) LIKE LOWER(CONCAT('%', :search, '%'))
    )
    """)
    Page<ProductVariantResponse> search(@Param("search") String search, Pageable pageable);

}

