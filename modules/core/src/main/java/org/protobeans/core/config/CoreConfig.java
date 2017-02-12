package org.protobeans.core.config;

import java.util.ArrayList;
import java.util.List;

import org.protobeans.core.annotation.InjectFromAnnotationBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreConfig {
    private static List<Class<?>> webAppContextConfigClasses = new ArrayList<>();
    
    public static void addWebAppContextConfigClass(Class<?> cls) {
        webAppContextConfigClasses.add(cls);
    }
    
    public static List<Class<?>> getWebAppContextConfigClasses() {
        return webAppContextConfigClasses;
    }
    
    @Bean
    public InjectFromAnnotationBeanPostProcessor injectFromParamProcessor() {
        return new InjectFromAnnotationBeanPostProcessor();
    }
}
