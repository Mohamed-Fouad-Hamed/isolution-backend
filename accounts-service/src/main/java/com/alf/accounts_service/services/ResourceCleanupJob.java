package com.alf.accounts_service.services;

import com.alf.accounts_service.models.ResourceFile;
import com.alf.accounts_service.repositories.ResourceFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResourceCleanupJob {

    private final ResourceFileRepository repo;
    private final FileStorageService storage;

    LocalDateTime threshold = LocalDateTime.now().minusDays(7);

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void cleanup() {

        log.info("[ResourceCleanupJob] started");

        List<ResourceFile> unused = repo.findInactiveOlderThan(threshold);

        unused.forEach(this::deleteOne);

        log.info("[ResourceCleanupJob] finished, deleted={}", unused.size());
    }

    @Transactional
    void deleteOne(ResourceFile r) {
        try {
            storage.delete(r.getPath());
            repo.delete(r);
        } catch (Exception e) {
            log.error("Failed to delete resource {}", r.getId(), e);
        }
    }
}

