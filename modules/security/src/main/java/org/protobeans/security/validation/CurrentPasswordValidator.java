package org.protobeans.security.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.protobeans.security.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CurrentPasswordValidator implements ConstraintValidator<CurrentPassword, String> {
    @Autowired
    private SecurityService securityService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void initialize(CurrentPassword constraintAnnotation) {
        //empty
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.trim().isEmpty()) return true;
        
        User currentUser = securityService.getCurrentUser();
        
        return passwordEncoder.matches(password, currentUser.getPassword());
    }
}
