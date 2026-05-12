package com.alf.media_service.repositories;

import com.alf.media_service.entities.ResourceFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceFileRepository extends JpaRepository<ResourceFile,String> {
}
