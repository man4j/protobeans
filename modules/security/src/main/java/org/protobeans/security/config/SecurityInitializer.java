package org.protobeans.security.config;

import java.nio.charset.StandardCharsets;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.filter.CharacterEncodingFilter;

import jakarta.servlet.ServletContext;

public class SecurityInitializer extends AbstractSecurityWebApplicationInitializer {
    @Override
    protected boolean enableHttpSessionEventPublisher() {
        return true;
    }

    @Override
    protected void beforeSpringSecurityFilterChain(ServletContext servletContext) {
        var encodingFilter = new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), 
                true, 
                true);

        var registration = servletContext.addFilter("encodingFilter", encodingFilter);
                                         
        registration.addMappingForUrlPatterns(null, false, "/*");
        registration.setAsyncSupported(true);
    }
}
