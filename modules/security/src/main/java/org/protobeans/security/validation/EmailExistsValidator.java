package org.protobeans.security.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.protobeans.security.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
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
        
        return profileService.getByEmail(email) == null;
    }
}
