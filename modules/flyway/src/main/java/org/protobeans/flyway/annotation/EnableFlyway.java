package org.protobeans.flyway.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.protobeans.flyway.config.FlywayConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(FlywayConfig.class)
@Configuration
public @interface EnableFlyway {
    String url() default "";
    
    String user() default "";
    
    String password() default "";
    
    boolean repair() default true;
    
    String migrationsPath() default "migrations";
    
    boolean validate() default true;
}
