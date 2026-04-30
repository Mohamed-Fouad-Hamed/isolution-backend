package com.alf.inventory.entity;

import com.alf.inventory.enums.OperationType;
import com.alf.inventory.enums.PickingState;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Picking")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Picking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(name="warehouse_id",nullable = false)
    private Long warehouseId;

    @Column(name="company_id",nullable = false)
    private Long companyId;

    @Enumerated(EnumType.STRING)
    @Column(name="operation_type",nullable = false, length = 30)
    private OperationType operationType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PickingState state;

    @Column(name="assigned_to")
    private Long assignedTo;

    @Builder.Default
    private Integer priority = 5;

    @Column(name="scheduled_at")
    private LocalDateTime scheduledAt;

    @Column(name="start_at")
    private LocalDateTime startedAt;

    @Column(name="completed_at")
    private LocalDateTime completedAt;

    private String reference;

    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="update_at")
    private LocalDateTime updatedAt;

    public boolean isCompleted() {
        return state == PickingState.DONE;
    }
}
