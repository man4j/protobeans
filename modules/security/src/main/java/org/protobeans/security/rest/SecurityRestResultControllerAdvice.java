package org.protobeans.security.rest;

import org.protobeans.exchange.model.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice(annotations = RestController.class)
@Order(50)
public class SecurityRestResultControllerAdvice {
    @Autowired HttpServletRequest request;
    
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAuthorizationException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(new RestResult(ex.getMessage()));
    }
    
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(new RestResult(ex.getMessage()));
    }
}
