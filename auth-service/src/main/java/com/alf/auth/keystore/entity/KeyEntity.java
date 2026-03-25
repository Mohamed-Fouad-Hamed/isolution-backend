package com.alf.auth.keystore.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;


@Entity
@Table(name = "jwt_keys")
@Data
public class KeyEntity {


    @Id
    @Column(columnDefinition = "uuid",length = 36)
    private UUID id; // UUID string


    @Column(nullable = false, unique = true)
    private String kid;


    @Lob
    @Column(nullable = false)
    private String publicKeyBase64; // X.509 base64


    @Lob
    @Column(nullable = false)
    private String privateKeyEncrypted; // encrypted base64


    @Column(nullable = false)
    private Instant createdAt;


    @Column(nullable = false)
    private boolean active;


    public KeyEntity() { this.id = UUID.randomUUID(); }



}
