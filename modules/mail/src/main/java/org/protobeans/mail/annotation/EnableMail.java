package org.protobeans.mail.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.protobeans.mail.config.MailConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(MailConfig.class)
@Configuration
public @interface EnableMail {
    String host() default "smtp.gmail.com";
    
    String user();
    
    String password();
}
