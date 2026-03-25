package com.alf.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "security.jwt")
@Getter
@Setter
public class JwtTokenProperties {

    int refreshTtlDays = 30 ;

    long accessTtlSeconds = 900;

}
