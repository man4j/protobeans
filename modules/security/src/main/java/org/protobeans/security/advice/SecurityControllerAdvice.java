package org.protobeans.security.advice;

import org.protobeans.security.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class SecurityControllerAdvice {
    @Autowired
    private SecurityService securityService;
    
    @ModelAttribute
    User getCurrentUser(@AuthenticationPrincipal User user) {
        return user;
    }
    
    @ModelAttribute
    SecurityService getSecurityService() {
        return securityService;
    }
}
