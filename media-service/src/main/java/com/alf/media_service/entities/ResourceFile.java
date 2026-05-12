package com.alf.media_service.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "resource_files")
public class ResourceFile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name="file_name")
    private String fileName;
    @Column(name="file_url")
    private String fileUrl;
    @Column(name = "thumbnail_url")
    private String thumbnailUrl;
    @Column(name="content_type")
    private String contentType; //  image/jpeg أو video/mp4
    @Column(name="file_size")
    private Long fileSize;

    @Column(name="created_at",updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
