package org.protobeans.postgresql.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.protobeans.postgresql.config.PostgreSqlConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(PostgreSqlConfig.class)
@Configuration
public @interface EnablePostgreSql {
    String dbHost();
    
    String dbPort() default "5432";
    
    String schema();
    
    String user();
    
    String password();
    
    int maxPoolSize() default 50; 
    
    String transactionIsolation() default "TRANSACTION_READ_COMMITTED";
}