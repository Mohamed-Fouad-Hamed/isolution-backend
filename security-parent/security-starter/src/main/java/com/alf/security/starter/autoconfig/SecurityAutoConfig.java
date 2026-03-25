package com.alf.security.starter.autoconfig;

import com.alf.security.common.SecurityProperties;
import com.alf.security.common.filters.JwtFilter;
import com.alf.security.starter.security.CustomAccessDeniedHandler;
import com.alf.security.starter.security.CustomAuthEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import lombok.RequiredArgsConstructor;


import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class SecurityAutoConfig {

    private final CustomAuthEntryPoint customAuthEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtFilter jwtFilter ,
                                                   SecurityProperties props) throws Exception {
        http.csrf(csrf -> csrf.disable());

        http.exceptionHandling(ex -> ex
                .authenticationEntryPoint(customAuthEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
        );

        http.authorizeHttpRequests(auth -> {

            auth.requestMatchers("/error").permitAll();
            // OPTIONS always allowed
            auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();

            List<String> excludedUrls = Optional.ofNullable(props.getFilterExcludeUrls())
                    .orElse(List.of())
                    .stream()
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toList();

            if (!excludedUrls.isEmpty()) {
                auth.requestMatchers(
                        excludedUrls.toArray(String[]::new)
                ).permitAll();
            }

            auth.anyRequest().authenticated();
        });

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /*
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Allow ALL headers from frontend (includes X-Device-Id)
        config.setAllowedHeaders(List.of("*"));

        config.setAllowCredentials(true);

        // Allow frontend to READ these headers from the response
        config.setExposedHeaders(List.of("Authorization", "X-Device-Id"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
     */
}
