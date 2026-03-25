package com.alf.accounts_service.config;


import com.alf.accounts_service.audit.SpringSecurityAuditorAware;
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