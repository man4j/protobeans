package org.protobeans.postgresql.config;

import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.postgresql.annotation.EnableJpaProfiler;
import org.protobeans.postgresql.aspect.JpaRepositoryMetricsAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@InjectFrom(EnableJpaProfiler.class)
public class JpaProfilerConfig {
    private String packageName;
    
    @Bean
    public JpaRepositoryMetricsAspect jpaRepositoryMetricsAspect() {
        return new JpaRepositoryMetricsAspect(packageName);
    }
}
