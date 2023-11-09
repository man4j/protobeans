package org.protobeans.security.validation;

import org.protobeans.security.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CurrentPasswordValidator implements ConstraintValidator<CurrentPassword, String> {
    @Autowired SecurityService securityService;
    
    @Autowired PasswordEncoder passwordEncoder;
    
    @Override
    public void initialize(CurrentPassword constraintAnnotation) {
        //empty
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.trim().isEmpty()) return true;
        
        return passwordEncoder.matches(password, securityService.getCurrentUser().getPassword());
    }
}
