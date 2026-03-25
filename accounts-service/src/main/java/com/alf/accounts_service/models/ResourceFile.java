package com.alf.accounts_service.models;


import com.alf.accounts_service.audit.Auditable;
import com.alf.accounts_service.models.enums.ResourceType;
import com.alf.accounts_service.models.enums.ResourceUsage;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "resource_files")
public class ResourceFile extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resource_files_id_seq")
    @SequenceGenerator(name = "resource_files_id_seq", sequenceName = "resource_files_id_seq", allocationSize=1)
    @Column(name = "id")
    private Long id;

    @Column(name = "file_name")
    private String filename;

    @Column(name = "file_type")
    private String fileType; // e.g., image/png, application/pdf

    @Column(name = "path")
    private String path; // relative path to file storage

    @Column(name = "size")
    private Long size;

    @Column(name = "active")
    private boolean active = true;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "resource_type")
    private ResourceType resourceType; // IMAGE, DOCUMENT, VIDEO, etc.

    @Enumerated(EnumType.STRING)
    @Column(name = "usage_type")
    private ResourceUsage usageType; // PROFILE_PIC, PRODUCT_GALLERY, REPORT_ATTACHMENT

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "owner_entity")
    private String ownerEntity;

}
