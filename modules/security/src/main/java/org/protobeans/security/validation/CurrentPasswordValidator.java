package org.protobeans.security.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.protobeans.security.model.AbstractProfile;
import org.protobeans.security.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CurrentPasswordValidator implements ConstraintValidator<CurrentPassword, String> {
    @Autowired
    private SecurityService securityService;
    
    @Autowired
    private ShaPasswordEncoder passwordEncoder;
    
    @Override
    public void initialize(CurrentPassword constraintAnnotation) {
        //empty
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.trim().isEmpty()) return true;
        
        AbstractProfile currentUser = securityService.getCurrentUser();
        
        return passwordEncoder.isPasswordValid(currentUser.getPassword(), password, currentUser.getEmail());
    }
}
