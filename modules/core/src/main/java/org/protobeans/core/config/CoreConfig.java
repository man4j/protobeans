package org.protobeans.core.config;

import org.protobeans.core.annotation.InjectFromAnnotationBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreConfig {
    @Bean
    public InjectFromAnnotationBeanPostProcessor injectFromParamProcessor() {
        return new InjectFromAnnotationBeanPostProcessor();
    }
}
