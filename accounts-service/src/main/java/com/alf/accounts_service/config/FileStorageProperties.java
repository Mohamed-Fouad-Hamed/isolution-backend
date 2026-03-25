package com.alf.accounts_service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.file")
@Getter
@Setter
public class FileStorageProperties {

    private Storage storage;

    @Getter @Setter
    public static class Storage {
        private String mapping;
    }
}
