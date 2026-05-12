package com.alf.inventory.repository;

import com.alf.inventory.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource,Long> {
}
