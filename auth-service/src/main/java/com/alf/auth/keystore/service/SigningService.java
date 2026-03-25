package com.alf.auth.keystore.service;
import com.alf.auth.keystore.config.AuthProperties;
import com.alf.auth.keystore.manager.DatabaseKeyManager;
import com.nimbusds.jose.JOSEException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SigningService {
    private final DatabaseKeyManager keyManager;
    private final AuthProperties authProperties;

    public String generateAccessToken(String subject,
                                      Map<String, Object> claims) throws JOSEException {
        PrivateKey privateKey = keyManager.current().toPrivateKey();
        Instant now = Instant.now();
        return Jwts.builder()
                .header()
                .add("kid", keyManager.current().getKeyID())
                .and()  // للخروج من ال header builder
                .claims()
                .id(UUID.randomUUID().toString())
                .subject(subject)
                .add(claims)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(authProperties.getAccessTtlSeconds())))
                .and() // الخروج من claims builder
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();
    }
    public String generateRefreshToken(String subject,
                                       String jti) throws JOSEException {
        PrivateKey privateKey = keyManager.current().toPrivateKey();
        Instant now = Instant.now();
        return Jwts.builder()
                .header()
                .add("kid", keyManager.current().getKeyID())
                .and()
                .claims()
                .id(jti)
                .subject(subject)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds( authProperties.getRefreshTtlDays() * 24 * 60 * 60 )))
                .and()
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();
    }

    public String hash(String token) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}