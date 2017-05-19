package org.protobeans.security.config;

import javax.servlet.Filter;

import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.core.config.CoreConfig;
import org.protobeans.security.annotation.EnableSecurity;
import org.protobeans.security.util.SecurityUrlsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.web.WebApplicationInitializer;

@Configuration
@InjectFrom(EnableSecurity.class)
public class SecurityConfig {
    static {
        CoreConfig.addWebAppContextConfigClass(SecurityWebConfig.class);
        CoreConfig.addWebAppContextConfigClass(GlobalMethodSecurityConfig.class);
    }

    private String[] ignoreUrls;
    
    private String loginUrl;
    
    @Autowired(required = false)
    private Filter[] filters = new Filter[] {};
    
    @Bean
    public SecurityUrlsBean securityUrlsBean() {
        return new SecurityUrlsBean(ignoreUrls, loginUrl);
    }
    
    @Bean
    public Class<? extends WebApplicationInitializer> securityInitializer() {
        for (Filter filter : filters) {
            SecurityWebConfig.addFilter(filter);
        }
        
        return SecurityInitializer.class; 
    }
    
    /**
     * We use old deprecated encoder for compatibility with TokenBasedRememberMeServices
     */
    @Bean
    public ShaPasswordEncoder passwordEncoder() {
        return new ShaPasswordEncoder();
    }
}
