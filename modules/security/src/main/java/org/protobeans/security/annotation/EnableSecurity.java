package org.protobeans.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.protobeans.security.config.GlobalMethodSecurityConfig;
import org.protobeans.security.config.SecurityConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({SecurityConfig.class, GlobalMethodSecurityConfig.class})
@Configuration
public @interface EnableSecurity {
    String[] ignoreUrls();
    
    String loginUrl();
}
