package org.protobeans.security.advice;

import org.protobeans.exchange.model.RestResult;
import org.protobeans.security.model.AbstractProfile;
import org.protobeans.security.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@Order(50)
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
    
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<Object> handleAuthorizationException(AuthorizationDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(new RestResult(ex.getMessage()));        
    }
    
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(new RestResult(ex.getMessage()));
    }
}
