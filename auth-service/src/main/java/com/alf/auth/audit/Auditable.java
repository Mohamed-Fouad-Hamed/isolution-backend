package com.alf.auth.audit;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class) // Enables JPA Auditing annotations
@Getter
@Setter
public abstract class Auditable {

    @CreatedBy
    @Column(name = "created_by", updatable = false, length = 255) // Explicit mapping if DB is snake_case
    private String createdBy; // Use camelCase

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate; // Use camelCase

    @LastModifiedBy
    @Column(name = "last_modified_by", length = 255)
    private String lastModifiedBy; // Use camelCase

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate; // Use camelCase

    // --- Soft Delete Fields ---
    // These fields are typically managed by @SQLDelete or the Service layer, not lifecycle callbacks here.

    @Column(name = "deleted_by", length = 255) // Match length with other user fields
    private String deletedBy; // Use camelCase

    @Column(name = "deleted_date")
    private LocalDateTime deletedDate; // Use camelCase

    @Column(name = "is_delete") // Or name="deleted"
    private boolean deleted = false; // Use primitive boolean, default false

}