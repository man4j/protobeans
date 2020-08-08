package org.protobeans.clickhouse.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.protobeans.clickhouse.config.ClickHouseConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(ClickHouseConfig.class)
@Configuration
public @interface EnableClickHouse {
    String dbHost();
    
    String dbPort() default "5432";
    
    String schema() default "default";
}