package org.protobeans.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.protobeans.core.annotation.InjectFromProcessor;

@Configuration
public class CoreConfig {
    @Bean
    public InjectFromProcessor injectFromParamProcessor() {
        return new InjectFromProcessor();
    }
}
