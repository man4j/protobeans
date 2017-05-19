package org.protobeans.mvc.config;

import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.core.config.CoreConfig;
import org.protobeans.mvc.annotation.EnableMvc;
import org.protobeans.mvc.util.ProtoBeansDefinitionScanner;
import org.protobeans.mvc.util.ResourcesVersionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;

@Configuration
@InjectFrom(EnableMvc.class)
public class MvcConfig {
    static {
        CoreConfig.addWebAppContextConfigClass(DispatcherServletContextConfig.class);
    }
    
    private String resourcesPath;
    
    private String resourcesUrl;
    
    private Class<?>[] basePackageClasses;
    
    @Autowired
    private ApplicationContext ctx;
    
    @Bean
    public ProtoBeansDefinitionScanner protoBeansDefinitionScanner() {
        return new ProtoBeansDefinitionScanner(basePackageClasses);
    }
    
    @Bean
    public Class<? extends WebApplicationInitializer> mvcInitializer() {
        MvcInitializer.rootApplicationContext = ctx;
        
        return MvcInitializer.class; 
    }
    
    @Bean
    public ResourcesVersionBean resourcesVersionBean() {
        return new ResourcesVersionBean(resourcesPath, resourcesUrl);
    }
}

