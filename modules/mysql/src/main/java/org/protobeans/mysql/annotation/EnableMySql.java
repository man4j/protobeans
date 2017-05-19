package org.protobeans.mysql.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.protobeans.mysql.config.MySqlConfig;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(MySqlConfig.class)
@Configuration
public @interface EnableMySql {
    String dbUrl();
    
    String schema();
    
    String user();
    
    String password();
    
    int maxPoolSize() default 50; 
}