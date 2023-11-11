package org.protobeans.mvc.config;

import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.mvc.annotation.EnableControllerProfiler;
import org.protobeans.mvc.interceptor.ControllerMetricsInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@InjectFrom(EnableControllerProfiler.class)
public class ControllerProfilerConfig {
    @Bean
    public ControllerMetricsInterceptor controllerMetricsInterceptor() {
        return new ControllerMetricsInterceptor();
    }
}
