package com.alf.auth.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "outbox_event")
public class OutboxEvent {

    @Id
    private String id;
    @Column(name = "aggregate_id")
    private String aggregateId; // userId
    @Column(name = "type_event")
    private String type;        // UserRegisteredEvent
    @Column(columnDefinition = "TEXT")
    private String payload;
    private boolean sent;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}