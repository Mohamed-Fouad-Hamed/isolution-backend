package com.alf.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "security.jwt")
public class SecurityProperties {
    private long jwksTtl;
    private long clockSkewSeconds;
    private List<String> filterExcludeUrls = new ArrayList<>();
    private String jwksUrl ;

    private boolean redisEnabled = false;

    public long getJwksTtl() {
        return jwksTtl;
    }

    public boolean isRedisEnabled() {
        return redisEnabled;
    }

    public void setRedisEnabled(boolean redisEnabled) {
        this.redisEnabled = redisEnabled;
    }

    public void setJwksTtl(long jwksTtl) {
        this.jwksTtl = jwksTtl;
    }

    public long getClockSkewSeconds() {
        return clockSkewSeconds;
    }

    public void setClockSkewSeconds(long clockSkewSeconds) {
        this.clockSkewSeconds = clockSkewSeconds;
    }

    public List<String> getFilterExcludeUrls() {
        return filterExcludeUrls;
    }

    public void setFilterExcludeUrls(List<String> filterExcludeUrls) {
        this.filterExcludeUrls = filterExcludeUrls;
    }

    public String getJwksUrl() {
        return jwksUrl;
    }

    public void setJwksUrl(String jwksUrl) {
        this.jwksUrl = jwksUrl;
    }
}
