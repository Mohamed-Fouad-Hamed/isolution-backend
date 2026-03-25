package com.alf.auth.keystore.rotation;

import com.alf.auth.keystore.manager.DatabaseKeyManager;
import com.alf.auth.keystore.service.KeyStoreService;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.security.KeyPairGenerator;
import java.security.KeyPair;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;


@Service
public class RotationService {
    private final KeyStoreService storeService;
    private final DatabaseKeyManager keyManager;
    private final StringRedisTemplate redis;
    private final int retainCount;
    private final String redisTopic;
    public RotationService(KeyStoreService storeService,
                           DatabaseKeyManager keyManager,
                           StringRedisTemplate redis,
                           org.springframework.core.env.Environment env) {
        this.storeService = storeService; this.keyManager = keyManager;
        this.redis = redis;
        this.retainCount =
                Integer.parseInt(env.getProperty("security.keystore.retain-keys","3"));
        this.redisTopic = env.getProperty("security.keystore.redis-topic","jwtkeys-updated");
    }

    @Scheduled(cron = "${security.keystore.rotation-cron:0 0 3 * * ?}")
    public void scheduledRotate() throws Exception {
        rotateAndPublish();
    }

    public synchronized void rotateAndPublish() throws Exception {

        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
        gen.initialize(2048);
        KeyPair pair = gen.generateKeyPair();
        String kid = UUID.randomUUID().toString();
        byte[] pub = pair.getPublic().getEncoded(); // X509
        byte[] priv = pair.getPrivate().getEncoded(); // PKCS8
        storeService.storeNewKey(kid, pub, priv);

        storeService.deactivateOldKeys(retainCount);

        redis.convertAndSend(redisTopic, "updated");

        keyManager.loadFromDb();
    }
}
