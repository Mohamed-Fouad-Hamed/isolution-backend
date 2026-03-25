package com.alf.gateway.config;

import com.alf.gateway.jwks.JwksCache;
import com.alf.gateway.jwt.JwtBlacklist;
import com.alf.gateway.jwt.JwtVerifierService;
import com.alf.gateway.token.TokenParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import reactor.core.publisher.Mono;

import java.net.URL;

@Configuration
public class GatewayConfig {
    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> Mono.just(
                exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
        );
    }

    @Bean
    public SecurityProperties securityProperties() {
        return new SecurityProperties();
    }

    @Bean
    public TokenParser tokenParser() {
        return new TokenParser();
    }

    @Bean
    public JwtBlacklist jwtBlacklist(
            @Autowired(required = false) StringRedisTemplate redis,
            SecurityProperties props) {

        boolean redisEnabled = props.isRedisEnabled();
        return new JwtBlacklist(redis, redisEnabled && redis != null);
    }

    @Bean
    public JwksCache jwksCache(SecurityProperties props) throws Exception {
        return new JwksCache(new URL(props.getJwksUrl()), props.getJwksTtl());
    }

    @Bean
    public JwtVerifierService jwtVerifierService(JwksCache cache,
                                                 SecurityProperties props) {
        return new JwtVerifierService(cache, props.getClockSkewSeconds());
    }


}