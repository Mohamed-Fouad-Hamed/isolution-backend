package com.alf.security.common.jwt;


import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class JwtBlacklist {
    private final StringRedisTemplate redis;
    private final ValueOperations<String, String> ops;
    private final ConcurrentMap<String, Long> memoryCache = new
            ConcurrentHashMap<>();
    private final boolean redisEnabled;

    public JwtBlacklist(StringRedisTemplate redis, boolean redisEnabled) {
        this.redis = redis;
        this.redisEnabled = redisEnabled;
        this.ops = redis != null ? redis.opsForValue() : null;
    }

    public void blacklist(String jti, long ttlSeconds) {
        if (redisEnabled && redis != null) {
            ops.set("bl:" + jti, "1", Duration.ofSeconds(ttlSeconds));
        }
        memoryCache.put(jti, System.currentTimeMillis() + ttlSeconds * 1000);
    }

    public boolean isBlacklisted(String jti) {
        if (redisEnabled && redis != null && ops.get("bl:" + jti) != null)
            return true;
        Long exp = memoryCache.get(jti);
        if (exp == null) return false;
        if (exp < System.currentTimeMillis()) {
            memoryCache.remove(jti);
            return false;
        }
        return true;
    }
}
