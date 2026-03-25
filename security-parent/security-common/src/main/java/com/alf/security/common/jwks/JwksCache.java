package com.alf.security.common.jwks;

import com.nimbusds.jose.jwk.JWKSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantReadWriteLock;
/**
 * Thread-safe JWKS cache with TTL and scheduled refresh + manual refresh.
 * - Construct with JWKS URL and TTL seconds.
 * - get() returns the cached JWKSet, loading from remote when expired.
 */
public class JwksCache {

    private static final Logger log = LoggerFactory.getLogger(JwksCache.class);
    private final URL jwksUrl;
    private final long ttlSeconds;
    private final AtomicReference<Cached> cached = new AtomicReference<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    public JwksCache(URL jwksUrl, long ttlSeconds) {
        this.jwksUrl = Objects.requireNonNull(jwksUrl, "jwksUrl");
        this.ttlSeconds = Math.max(1, ttlSeconds);
    }
    public JWKSet get() {
        // fast path
        Cached c = cached.get();
        if (c != null && !c.isExpired()) return c.jwkSet;
        // slow path: load under write lock
        lock.writeLock().lock();
        try {
            c = cached.get();
            if (c == null || c.isExpired()) {
                log.info("JWKS cache miss or expired - loading from {}",
                        jwksUrl);
                try {
                    JWKSet set = JWKSet.load(jwksUrl);
                    cached.set(new Cached(set,
                            Instant.now().plusSeconds(ttlSeconds)));
                    log.info("Loaded JWKS ({} keys)", set.getKeys().size());
                } catch (Exception e) {
                    log.error("Failed to load JWKS from {}", jwksUrl, e);
                    // if we had a previously cached value, keep it (gracefuldegrade)
                    if (cached.get() == null) throw new
                            RuntimeException("Cannot load JWKS", e);
                }
            }
            return cached.get().jwkSet;
        } finally {
            lock.writeLock().unlock();
        }
    }
    /**
     * Force refresh now (synchronous). Useful for admin-triggered rotation
     notification.
     */
    public void refresh() {
        lock.writeLock().lock();
        try {
            log.info("Forced JWKS refresh from {}", jwksUrl);
            JWKSet set = JWKSet.load(jwksUrl);
            cached.set(new Cached(set, Instant.now().plusSeconds(ttlSeconds)));
        } catch (Exception e) {
            log.error("Forced JWKS refresh failed", e);
            throw new RuntimeException(e);
        } finally {
            lock.writeLock().unlock();
        }
    }
    /**
     * Optional scheduled refresh - call this from a @Scheduled method or enable
     scheduling here.
     */
    @Scheduled(fixedDelayString = "PT5M") // default every 5 minutes; can beadjusted by overriding method in config
    public void scheduledRefresh() {
        try {
            refresh();
        } catch (Exception e) {
            log.warn("Scheduled JWKS refresh failed: {}", e.getMessage());
        }
    }
    private static class Cached {
        final JWKSet jwkSet;
        final Instant expiry;
        Cached(JWKSet jwkSet, Instant expiry) {
            this.jwkSet = jwkSet; this.expiry = expiry;
        }
        boolean isExpired() { return Instant.now().isAfter(expiry); }
    }
}