package org.protobeans.security.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.protobeans.security.model.AbstractProfile;
import org.protobeans.security.model.SignInForm;
import org.protobeans.security.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SignInValidator implements ConstraintValidator<SignIn, SignInForm> {
    @Autowired
    private ProfileService profileService;
    
    @Autowired
    private ShaPasswordEncoder passwordEncoder;
    
    @Override
    public void initialize(SignIn constraintAnnotation) {
        //empty
    }

    @Override
    public boolean isValid(SignInForm form, ConstraintValidatorContext context) {
        if (form.getEmail() == null || form.getEmail().trim().isEmpty()) return true;
        if (form.getPassword() == null || form.getPassword().trim().isEmpty()) return true;
        
        context.disableDefaultConstraintViolation();

        AbstractProfile profile = profileService.getByEmail(form.getEmail());

        if (profile == null || !passwordEncoder.isPasswordValid(profile.getPassword(), form.getPassword(), form.getEmail())) {
            context.buildConstraintViolationWithTemplate("{signin.incorrectEmailOrPassword}").addPropertyNode("email").addConstraintViolation();
            context.buildConstraintViolationWithTemplate("").addPropertyNode("password").addConstraintViolation();
            
            return false;
        }
        
        if (!profile.isConfirmed()) {
            context.buildConstraintViolationWithTemplate("{signin.notConfirmed}").addConstraintViolation();
            
            return false;
        }
        
        return true;
    }
}
