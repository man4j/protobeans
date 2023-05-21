package org.protobeans.security.advice;

import org.protobeans.security.model.AbstractProfile;
import org.protobeans.security.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class SecurityControllerAdvice {
    @Autowired
    private SecurityService securityService;
    
    @ModelAttribute(name = "user")
    AbstractProfile getCurrentUser() {
        return securityService.getCurrentUser();
    }
    
    @ModelAttribute
    SecurityService getSecurityService() {
        return securityService;
    }
}
