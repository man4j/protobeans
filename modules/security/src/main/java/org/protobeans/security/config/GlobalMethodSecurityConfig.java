package org.protobeans.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true, proxyTargetClass = true)
public class GlobalMethodSecurityConfig extends GlobalMethodSecurityConfiguration {
    @Autowired(required = false)
    private PermissionEvaluator permissionEvaluator;
    
    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler defaultMethodExpressionHandler = new DefaultMethodSecurityExpressionHandler();
        
        if (permissionEvaluator != null) {
            defaultMethodExpressionHandler.setPermissionEvaluator(permissionEvaluator);
        }
        
        return defaultMethodExpressionHandler;
    }
}
