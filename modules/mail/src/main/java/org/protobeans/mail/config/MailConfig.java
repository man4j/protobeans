package org.protobeans.mail.config;

import java.util.Properties;

import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.mail.annotation.EnableMail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@InjectFrom(EnableMail.class)
public class MailConfig {
    private String host;
    
    private String user;
    
    private String password;
    
    @Bean
    public JavaMailSender javaMailService() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        
        javaMailSender.setHost(host);
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
