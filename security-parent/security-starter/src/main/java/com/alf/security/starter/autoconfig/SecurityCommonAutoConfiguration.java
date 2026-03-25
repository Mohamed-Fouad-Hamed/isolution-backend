package com.alf.security.starter.autoconfig;


import com.alf.security.common.SecurityProperties;
import com.alf.security.common.filters.JwtFilter;
import com.alf.security.common.jwks.JwksCache;
import com.alf.security.common.jwt.JwtBlacklist;
import com.alf.security.common.jwt.JwtVerifierService;
import com.alf.security.common.token.TokenParser;
import com.alf.security.starter.security.CustomAccessDeniedHandler;
import com.alf.security.starter.security.CustomAuthEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.net.URL;


@AutoConfiguration
@EnableConfigurationProperties(SecurityProperties.class)
@ConditionalOnProperty(prefix = "security.jwt", name = "enabled", havingValue = "true", matchIfMissing = true)
public class SecurityCommonAutoConfiguration {


    @Bean
    public CustomAuthEntryPoint customAuthEntryPoint() {
        return new CustomAuthEntryPoint();
    }

    @Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public TokenParser tokenParser() {
        return new TokenParser();
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtBlacklist jwtBlacklist(
            @Autowired(required = false) StringRedisTemplate redis,
            SecurityProperties props) {

        boolean redisEnabled = props.isRedisEnabled();
        return new JwtBlacklist(redis, redisEnabled && redis != null);
    }

    @Bean
    @ConditionalOnMissingBean
    public JwksCache jwksCache(SecurityProperties props) throws Exception {
        return new JwksCache(new URL(props.getJwksUrl()), props.getJwksTtl());
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtVerifierService jwtVerifierService(JwksCache cache,
                                                 SecurityProperties props) {
        return new JwtVerifierService(cache, props.getClockSkewSeconds());
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtFilter jwtFilter(JwtVerifierService verifier,
                               JwtBlacklist blacklist,
                               TokenParser parser,
                               SecurityProperties props,
                               HandlerExceptionResolver handlerExceptionResolver) {
        return new JwtFilter(verifier, blacklist, parser, props, handlerExceptionResolver);
    }

}
