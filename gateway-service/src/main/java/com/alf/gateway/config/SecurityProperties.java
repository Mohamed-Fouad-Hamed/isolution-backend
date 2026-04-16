package com.alf.gateway.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ConfigurationProperties(prefix = "security.jwt")
public class SecurityProperties {
    private long jwksTtl;
    private long clockSkewSeconds;
    private List<String> filterExcludeUrls = new ArrayList<>();
    private String jwksUrl ;

    private boolean redisEnabled = false;

}
