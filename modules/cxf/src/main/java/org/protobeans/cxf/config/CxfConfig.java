package org.protobeans.cxf.config;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.cxf.annotation.EnableCxf;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ConfigurableWebApplicationContext;

@Configuration
@InjectFrom(EnableCxf.class)
public class CxfConfig {
    private String servletPath;
    
    @Bean(name=Bus.DEFAULT_BUS_ID)//hard coded bus name need for CXFServlet
    public SpringBus springBus() {
        return new SpringBus();
    }
        
    @Bean
    public Class<? extends WebApplicationInitializer> cxfInitializer(ConfigurableWebApplicationContext ctx) {
        CxfInitializer.SERVLET_PATH = servletPath;
        CxfInitializer.rootApplicationContext = ctx;
        
        return CxfInitializer.class; 
    }
}
