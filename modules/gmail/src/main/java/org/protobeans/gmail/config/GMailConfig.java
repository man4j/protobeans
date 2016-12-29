package org.protobeans.gmail.config;

import java.util.Properties;

import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.gmail.annotation.EnableGMail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@InjectFrom(EnableGMail.class)
public class GMailConfig {
    private String user;
    
    private String password;
    
    @Bean
    public JavaMailSender javaMailService() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setPort(587);
        javaMailSender.setUsername(user);
        javaMailSender.setPassword(password);
        
        Properties properties = new Properties();
        
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");

        javaMailSender.setJavaMailProperties(properties);        
        
        return javaMailSender;
    }
}
