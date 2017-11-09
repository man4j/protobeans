package org.protobeans.mysql.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.protobeans.mysql.config.MySqlConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(MySqlConfig.class)
@Configuration
public @interface EnableMySql {
    String dbHost();
    
    String dbPort() default "3306";
    
    String schema();
    
    String user();
    
    String password();
    
    int maxPoolSize() default 50; 
}