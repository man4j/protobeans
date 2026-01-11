package org.protobeans.webapp.example.validation;

import org.protobeans.security.service.SecurityService;
import org.protobeans.webapp.example.model.SignInOttForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ott.OneTimeTokenAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SignInOttValidator implements ConstraintValidator<SignInOtt, SignInOttForm> {
    @Autowired AuthenticationManager authenticationManager;
    
    @Autowired SecurityService securityService;
    
    @Override
    public void initialize(SignInOtt constraintAnnotation) {
        //empty
    }

    @Override
    public boolean isValid(SignInOttForm form, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        
        if (form.getToken() == null || form.getToken().trim().isEmpty()) return true;
        
        try {
            var token = new OneTimeTokenAuthenticationToken(form.getToken());
            var auth = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (AuthenticationException _) {
            context.buildConstraintViolationWithTemplate("{SignInOttValidator.incorrectToken").addPropertyNode("token").addConstraintViolation();
            
            return false;
        }
        
        return true;
    }
}