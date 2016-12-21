package org.protobeans.mvc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;
import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.mvc.annotation.EnableMvc;
import org.protobeans.mvc.util.ResourcesVersionBean;

@Configuration
@InjectFrom(EnableMvc.class)
public class MvcConfig {
    private String resourcesPath;
    
    private String resourcesUrl;
    
    private Class<?>[] basePackageClasses;
    
    @Autowired
    private ApplicationContext ctx;
    
    @Bean
    public Class<? extends WebApplicationInitializer> mvcInitializer() {
        MvcInitializer.rootApplicationContext = ctx;
        MvcInitializer.basePackageClasses = basePackageClasses;
        
        return MvcInitializer.class; 
    }
    
    @Bean
    public ResourcesVersionBean resourcesVersionBean() {
        return new ResourcesVersionBean(resourcesPath, resourcesUrl);
    }
}

