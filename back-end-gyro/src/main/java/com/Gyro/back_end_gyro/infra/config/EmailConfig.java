package com.Gyro.back_end_gyro.infra.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Value("${SPRING_MAIL_HOST}")
    private String host;

    @Value("${SPRING_MAIL_PORT}")
    private int port;

    @Value("${SPRING_MAIL_USERNAME}")
    private String username;

    @Value("${SPRING_MAIL_PASSWORD}")
    private String password;

    @Value("${SPRING_MAIL_PROPERTIES_MAIL_SMTP_AUTH}")
    private String smtpAuth;

    @Value("${SPRING_MAIL_PROPERTIES_MAIL_SMTP_SSL_ENABLE}")
    private String sslEnable;

    @Value("${SPRING_MAIL_PROPERTIES_MAIL_SMTP_STARTTLS_ENABLE}")
    private String starttlsEnable;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", smtpAuth);
        props.put("mail.smtp.ssl.enable", sslEnable);
        props.put("mail.smtp.starttls.enable", starttlsEnable);
        props.put("mail.transport.protocol", "smtp");

        return mailSender;
    }
}