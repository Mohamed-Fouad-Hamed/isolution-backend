package com.alf.inventory.repository;

import com.alf.inventory.entity.UomGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UomGroupRepository
        extends JpaRepository<UomGroup, Long> {

    Optional<UomGroup> findByCode(String code);

}

