package com.alf.auth.keystore.service;

import com.alf.auth.keystore.config.SecurityKeystoreProperties;
import com.alf.auth.keystore.entity.KeyEntity;
import com.alf.auth.keystore.repo.KeyRepository;
import com.alf.auth.keystore.util.AesGcmUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class KeyStoreService {

    private static final Logger log = LoggerFactory.getLogger(KeyStoreService.class);
    private final KeyRepository repo;
    private final byte[] encryptionKey; // raw bytes 32

    private final SecurityKeystoreProperties props;


    public KeyStoreService(SecurityKeystoreProperties props, KeyRepository repo) {
        this.props = props;
        this.repo = repo;
        this.encryptionKey = Base64.getDecoder().decode(this.props.getEncryptionPassword());

         log.info("===================== START KEY Store Service ================== Value |  " + this.encryptionKey );
        try {
            if (this.encryptionKey.length != 32) throw new IllegalArgumentException("encryption key must be 32 bytes (base64)");

        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize KeyStoreService: " + e.getMessage(), e);
        }
        log.info("===================== END KEY Store Service ================== Length " + this.encryptionKey.length);
    }

    public boolean hasAnyKey() {
        return repo.count() > 0;
    }
    @Transactional
    public KeyEntity storeNewKey(String kid, byte[] pubX509, byte[] privPkcs8) throws Exception {
        String pubB64 = Base64.getEncoder().encodeToString(pubX509);
        String privEncrypted = AesGcmUtil.encrypt(encryptionKey, privPkcs8);

        KeyEntity e = new KeyEntity();
        e.setKid(kid);
        e.setPublicKeyBase64(pubB64);
        e.setPrivateKeyEncrypted(privEncrypted);
        e.setCreatedAt(java.time.Instant.now());
        e.setActive(true);
        return repo.save(e);
    }


    @Transactional(readOnly = true)
    public List<KeyEntity> getActiveKeys() {
        return repo.findByActiveTrueOrderByCreatedAtDesc();
    }


    @Transactional(readOnly = true)
    public byte[] decryptPrivate(KeyEntity e) throws Exception {
        return AesGcmUtil.decrypt(encryptionKey, e.getPrivateKeyEncrypted());
    }


    @Transactional
    public void deactivateOldKeys(int retain) {
        var keys = repo.findAll();
// sort desc by createdAt
        var sorted = keys.stream().sorted((a,b) -> b.getCreatedAt().compareTo(a.getCreatedAt())).collect(Collectors.toList());
        for (int i = retain; i < sorted.size(); i++){
            var ke = sorted.get(i);
            if (ke.isActive()) { ke.setActive(false); repo.save(ke); }
        }
    }
}