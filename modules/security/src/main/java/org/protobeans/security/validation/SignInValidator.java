package org.protobeans.security.validation;

import org.protobeans.security.model.SignInForm;
import org.protobeans.security.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SignInValidator implements ConstraintValidator<SignIn, SignInForm> {
    @Autowired AuthenticationManager authenticationManager;
    
    @Autowired SecurityService securityService;
    
    @Override
    public void initialize(SignIn constraintAnnotation) {
        //empty
    }

    @Override
    public boolean isValid(SignInForm form, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        
        if (form.getLogin() == null || form.getLogin().trim().isEmpty()) return true;
        if (form.getPassword() == null || form.getPassword().trim().isEmpty()) return true;
        
        try {
            var token = new UsernamePasswordAuthenticationToken(form.getLogin(), form.getPassword());
            var auth = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (AuthenticationException _) {
            context.buildConstraintViolationWithTemplate("{SignInValidator.incorrectLoginOrPassword}").addPropertyNode("login").addConstraintViolation();
            context.buildConstraintViolationWithTemplate("").addPropertyNode("password").addConstraintViolation();
            
            return false;
        }
        
        return true;
    }
}