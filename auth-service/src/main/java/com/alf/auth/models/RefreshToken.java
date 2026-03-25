package com.alf.auth.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "refresh_tokens", indexes = {
        @Index(name = "idx_refresh_user_device", columnList = "login, device_id")})
public class RefreshToken {

    @Id
    private String id;

    @Column(nullable = false)
    private String login;

    @Column(nullable = false, unique = true)
    private String tokenHash; // sha256 of the raw refresh token

    @Column(nullable = false)
    private Instant expiryDate;

    @Column(nullable = false)
    private boolean used = false;

    @Column(nullable = false)
    private boolean revoked = false;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @Column
    private String deviceId; // optional: bind token to device

}