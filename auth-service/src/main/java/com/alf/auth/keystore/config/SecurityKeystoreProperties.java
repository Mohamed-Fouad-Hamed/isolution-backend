package com.alf.auth.keystore.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "security.keystore")
public class SecurityKeystoreProperties {

    private String encryptionPassword;
    private String redisTopic = "jwt-keys-updated";

    public String getEncryptionPassword() {
        return encryptionPassword;
    }

    public void setEncryptionPassword(String encryptionPassword) {
        this.encryptionPassword = encryptionPassword;
    }

    public String getRedisTopic() {
        return redisTopic;
    }

    public void setRedisTopic(String redisTopic) {
        this.redisTopic = redisTopic;
    }
}
