package org.protobeans.security.validation;

import org.protobeans.security.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailExistsValidator implements ConstraintValidator<EmailExists, String> {
    @Autowired
    private ProfileService profileService;
    
    @Override
    public void initialize(EmailExists constraintAnnotation) {
        //empty
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.trim().isEmpty()) return true;
        
        return profileService.getById(email) != null;
    }
}
