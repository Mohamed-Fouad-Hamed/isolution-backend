package com.alf.auth.config;

import com.alf.auth.audit.SpringSecurityAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware") // Reference the bean name
public class PersistenceConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return new SpringSecurityAuditorAware(); // Your implementation
    }
}