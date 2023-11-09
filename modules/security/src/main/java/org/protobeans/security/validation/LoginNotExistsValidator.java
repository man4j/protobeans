package org.protobeans.security.validation;

import org.protobeans.security.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LoginNotExistsValidator implements ConstraintValidator<LoginNotExists, String> {
    @Autowired ProfileService profileService;
    
    @Override
    public void initialize(LoginNotExists constraintAnnotation) {
        //empty
    }

    @Override
    public boolean isValid(String login, ConstraintValidatorContext context) {
        if (login == null || login.trim().isEmpty()) return true;
        
        return profileService.getByLogin(login) == null;
    }
}
