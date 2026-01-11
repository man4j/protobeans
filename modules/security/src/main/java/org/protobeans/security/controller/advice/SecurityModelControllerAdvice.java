package org.protobeans.security.controller.advice;

import org.protobeans.security.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@Order(50)
public class SecurityModelControllerAdvice {
    @Autowired
    private SecurityService securityService;
    
    @ModelAttribute(name = "principal")
    String getCurrentUser() {
        return securityService.getCurrentPrincipal();
    }
    
    @ModelAttribute
    SecurityService getSecurityService() {
        return securityService;
    }
}
