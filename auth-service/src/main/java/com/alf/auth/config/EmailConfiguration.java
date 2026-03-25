package com.alf.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfiguration {

    @Autowired
    private EmailProviderConfig emailProviderConfig;
/*
    @Bean
    public JavaMailSender mailSender()
    {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(emailProviderConfig.getHost());
        javaMailSender.setPort(emailProviderConfig.getPort());

        javaMailSender.setUsername(emailProviderConfig.getUsername());
        javaMailSender.setPassword(emailProviderConfig.getPassword());

        Properties properties = javaMailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.debug", emailProviderConfig.getDebug().toString());

        return javaMailSender;
    }
    */

}
