package org.protobeans.monitoring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.protobeans.monitoring.config.ServiceMonitoringConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(ServiceMonitoringConfig.class)
@Configuration
public @interface EnableServiceMonitoring {
    String dnsAddr();
}
