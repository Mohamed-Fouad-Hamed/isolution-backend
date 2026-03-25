package com.alf.accounts_service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
@RequiredArgsConstructor
public class ResourceWebConfig implements WebMvcConfigurer {

    private final FileStorageProperties props;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String location = props.getStorage().getMapping();

        if (location == null || location.isBlank()) {
            throw new IllegalStateException(
                    "app.file.storage.mapping is not configured"
            );
        }


        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(location)
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }

}
