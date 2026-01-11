package org.protobeans.security.config;

import org.protobeans.security.service.SecurityService;
import org.protobeans.security.validation.CurrentPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.spel.spi.EvaluationContextExtension;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.web.WebApplicationInitializer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true, proxyTargetClass = true)
@ComponentScan(basePackageClasses={SecurityService.class, CurrentPassword.class})
public class CommonSecurityConfig {
    @Autowired
    private ApplicationContext ctx;
    
    @Autowired(required = false)
    private PermissionEvaluator permissionEvaluator;
    
    @Bean
    public Class<? extends WebApplicationInitializer> securityInitializer() {
        return SecurityInitializer.class;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    
    @Bean
    public MethodSecurityExpressionHandler createExpressionHandler() {
        var defaultMethodExpressionHandler = new DefaultMethodSecurityExpressionHandler();
        
        if (permissionEvaluator != null) {
            defaultMethodExpressionHandler.setPermissionEvaluator(permissionEvaluator);
            defaultMethodExpressionHandler.setApplicationContext(ctx);
        }
        
        return defaultMethodExpressionHandler;
    }
    
    @Bean
    public EvaluationContextExtension securityExtension() {
        return new SecurityEvaluationContextExtension();
    }
    
    @Bean
    public SecurityService securityService() {
        return new SecurityService();
    }
}
