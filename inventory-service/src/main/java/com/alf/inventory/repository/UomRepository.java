package com.alf.inventory.repository;

import com.alf.inventory.entity.Uom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UomRepository extends JpaRepository<Uom, Long> {

    Optional<Uom> findByCode(String code);

}
