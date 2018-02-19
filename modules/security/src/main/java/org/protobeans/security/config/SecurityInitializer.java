package org.protobeans.security.config;

import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

@Order(10)//для того, чтобы фильтры SpringSecurity инициализировались позже фильтров WebMVC
public class SecurityInitializer extends AbstractSecurityWebApplicationInitializer {
    @Override
    protected boolean enableHttpSessionEventPublisher() {
        return true;
    }
}
