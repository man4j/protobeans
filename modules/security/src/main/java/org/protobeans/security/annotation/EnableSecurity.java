package org.protobeans.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.protobeans.security.config.SecurityConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(SecurityConfig.class)
@EnableWebSecurity
@Configuration
public @interface EnableSecurity {
    //empty
}
