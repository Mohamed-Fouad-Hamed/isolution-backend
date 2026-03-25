package com.alf.auth.keystore.manager;

import com.alf.auth.keystore.entity.KeyEntity;
import com.alf.auth.keystore.service.KeyStoreService;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;


@Component("dbKeyManager")
public class DatabaseKeyManager {


    private final KeyStoreService storeService;
    private final AtomicReference<LinkedHashMap<String, RSAKey>> cache = new AtomicReference<>(new LinkedHashMap<>());


    @Getter
    private String activeKid;

    public DatabaseKeyManager(KeyStoreService storeService) {
        this.storeService = storeService;
        ensureDefaultKeyExists();
        loadFromDb();
    }


    public synchronized void loadFromDb() {
        try {
            List<KeyEntity> keys = storeService.getActiveKeys();
            LinkedHashMap<String, RSAKey> map = new LinkedHashMap<>();
            for (KeyEntity e : keys) {

                byte[] pub = Base64.getDecoder().decode(e.getPublicKeyBase64());
                byte[] priv = storeService.decryptPrivate(e);


                KeyFactory kf = KeyFactory.getInstance("RSA");
                RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(pub));
                RSAPrivateKey privKey = (RSAPrivateKey) kf.generatePrivate(new PKCS8EncodedKeySpec(priv));


                RSAKey rsa = new RSAKey.Builder(pubKey).privateKey(privKey).keyID(e.getKid()).build();
                map.put(e.getKid(), rsa);

            }

            cache.set(map);

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }



    public void ensureDefaultKeyExists() {
        if (storeService.hasAnyKey()) return;
        try {
            KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
            gen.initialize(2048);
            KeyPair pair = gen.generateKeyPair();
            String kid = UUID.randomUUID().toString();
            byte[] pub = pair.getPublic().getEncoded(); // X509
            byte[] priv = pair.getPrivate().getEncoded(); // PKCS8
            storeService.storeNewKey(kid, pub, priv);
            activeKid = kid;
            System.out.println("[KeyManager] Default RSA key generated successfully with KID=" + kid);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to generate default RSA key", ex);
        }
    }



    public RSAKey current() {
        var m = cache.get();
        if (m.isEmpty()) throw new IllegalStateException("no keys");
        return m.values().iterator().next();
    }


    public JWKSet publicJwkSet() {
        var m = cache.get();
        List<JWK> pub = new ArrayList<>();
        for (var r : m.values()) pub.add(r.toPublicJWK());
        return new JWKSet(pub);
    }


    public RSAKey getByKid(String kid){ return cache.get().get(kid); }
}
