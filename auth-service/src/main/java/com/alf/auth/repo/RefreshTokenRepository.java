package com.alf.auth.repo;

import com.alf.auth.models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    Optional<RefreshToken> findByTokenHash(String tokenHash);

    // لإيجاد توكن مرتبط بجهاز و user
    Optional<RefreshToken> findByLoginAndDeviceId(String login, String deviceId);

    // لحذف كل التوكنات الخاصة بجهاز (logout all tokens for device)
    void deleteByLoginAndDeviceId(String login, String deviceId);

}

