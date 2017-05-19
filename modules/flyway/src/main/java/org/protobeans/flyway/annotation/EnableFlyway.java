package org.protobeans.flyway.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.protobeans.flyway.config.FlywayConfig;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(FlywayConfig.class)
@Configuration
public @interface EnableFlyway {
    String dbUrl();
    
    String schema();
    
    String user();
    
    String password();
    
    boolean waitDb() default false;
}
