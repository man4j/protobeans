package org.protobeans.webservices.config;

import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.webservices.annotation.EnableWebServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;

@Configuration
@InjectFrom(EnableWebServices.class)
public class WebServicesConfig {
    private String[] mappings;
    
    private Class<?>[] configClasses;
    
    @Autowired
    private ApplicationContext ctx;
    
    @Bean
    public Class<? extends WebApplicationInitializer> wsInitializer() {
        WsServletInitializer.rootApplicationContext = ctx;
        WsServletInitializer.mappings = mappings;
        WsServletInitializer.configClasses = configClasses;
        
        return WsServletInitializer.class; 
    }
}
