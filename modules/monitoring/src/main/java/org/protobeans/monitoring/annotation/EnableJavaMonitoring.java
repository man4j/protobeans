package org.protobeans.monitoring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.protobeans.monitoring.config.JavaMonitoringConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(JavaMonitoringConfig.class)
@Configuration
public @interface EnableJavaMonitoring {
    int interval() default 15;
}
