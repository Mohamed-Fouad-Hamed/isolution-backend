package com.alf.accounts_service.repositories;


import com.alf.accounts_service.models.ResourceFile;
import com.alf.accounts_service.models.enums.ResourceType;
import com.alf.accounts_service.models.enums.ResourceUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ResourceFileRepository extends JpaRepository<ResourceFile, Long> {

    List<ResourceFile> findByResourceTypeAndOwnerEntityAndOwnerIdAndUsageTypeAndActiveTrue(ResourceType resourceType, String ownerEntity, Long ownerId, ResourceUsage usageType);
    List<ResourceFile> findByOwnerEntityAndOwnerIdAndUsageType(String ownerEntity, Long ownerId, ResourceUsage usageType);

    List<ResourceFile> findByOwnerEntityAndOwnerIdAndActiveTrue(
            String ownerEntity,
            Long ownerId
    );

    List<ResourceFile> findByOwnerEntityAndOwnerIdAndUsageTypeAndActiveTrue(
            String ownerEntity,
            Long ownerId,
            ResourceUsage usageType
    );

    Optional<ResourceFile> findByIdAndActiveTrue(Long id);

    @Query("""
        select r
        from ResourceFile r
        where r.active = false
          and r.lastModifiedDate < :threshold
    """)
    List<ResourceFile> findInactiveOlderThan(@Param("threshold") LocalDateTime threshold);

}
