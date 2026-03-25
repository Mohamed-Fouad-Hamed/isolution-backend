package com.alf.auth.keystore.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "security.jwt")
@Getter
@Setter
public class AuthProperties {
    private long refreshTtlDays = 30L;
    private long accessTtlSeconds = 900L;
}
