package com.alf.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "email")
@Getter
@Setter
public class EmailProviderConfig {
    private String host;
    private Integer port;
    private String username;
    private String password;
    private Boolean debug;
}
