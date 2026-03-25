package com.alf.auth.config;

import com.alf.auth.models.OutboxEvent;
import com.alf.auth.repo.OutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxPublisher {

    private final OutboxRepository outboxRepo;
    private final KafkaTemplate<String, String> kafka;

    @Scheduled(fixedDelay = 5000)
    @Transactional // Ensures DB update and loop consistency
    public void publishEvents() {
        // 1. Fetch events that haven't been sent yet
        List<OutboxEvent> events = outboxRepo.findBySentFalse();

        if (events.isEmpty()) {
            return;
        }

        log.info("Processing {} outbox events...", events.size());

        for (OutboxEvent event : events) {
            try {
                // 2. Send the raw String payload to Kafka
                // event.getType() is used as the TOPIC name
                // event.getPayload() is the MESSAGE body
                kafka.send(event.getType(), event.getPayload());

                // 3. Mark as sent and update the database
                event.setSent(true);
                outboxRepo.save(event);

            } catch (Exception e) {
                log.error("Failed to send event {}: {}", event.getId(), e.getMessage());
                // We don't throw the exception here so that one failing message
                // doesn't block other messages in the same batch.
            }
        }
    }
}

