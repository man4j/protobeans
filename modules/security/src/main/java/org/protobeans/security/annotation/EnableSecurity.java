package org.protobeans.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.protobeans.security.config.CommonSecurityConfig;
import org.protobeans.security.config.SecurityConfig;
import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({CommonSecurityConfig.class, SecurityConfig.class})
public @interface EnableSecurity {
    String loginFormUrl() default "/signin";
    
    String accessDeniedUrl() default "/forbidden";
    
    String rememberMeKey() default "changeme";
}
