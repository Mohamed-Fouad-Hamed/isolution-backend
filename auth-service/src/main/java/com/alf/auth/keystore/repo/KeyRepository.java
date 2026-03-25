package com.alf.auth.keystore.repo;


import com.alf.auth.keystore.entity.KeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface KeyRepository extends JpaRepository<KeyEntity, String> {
    List<KeyEntity> findByActiveTrueOrderByCreatedAtDesc();
}
