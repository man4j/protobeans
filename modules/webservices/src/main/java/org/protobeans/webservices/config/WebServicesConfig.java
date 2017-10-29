package org.protobeans.webservices.config;

import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.webservices.annotation.EnableWebServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ConfigurableWebApplicationContext;

@Configuration
@InjectFrom(EnableWebServices.class)
public class WebServicesConfig {
    @Bean
    public Class<? extends WebApplicationInitializer> wsInitializer(ConfigurableWebApplicationContext ctx) {
        WsServletInitializer.rootApplicationContext = ctx;
        
        return WsServletInitializer.class; 
    }
}
