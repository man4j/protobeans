package org.protobeans.security.validation;

import org.protobeans.security.model.AbstractProfile;
import org.protobeans.security.model.SignInForm;
import org.protobeans.security.service.ProfileService;
import org.protobeans.security.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SignInValidator implements ConstraintValidator<SignIn, SignInForm> {
    @Autowired
    private ProfileService profileService;
    
    @Autowired
    private SecurityService securityService;
    
    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;
    
    @Override
    public void initialize(SignIn constraintAnnotation) {
        //empty
    }

    @Override
    public boolean isValid(SignInForm form, ConstraintValidatorContext context) {
        if (form.getLogin() == null || form.getLogin().trim().isEmpty()) return true;
        if (form.getPassword() == null || form.getPassword().trim().isEmpty()) return true;
        
        context.disableDefaultConstraintViolation();

        AbstractProfile profile = profileService.getByLogin(form.getLogin());
        
        try {
            if (profile == null) throw new UsernameNotFoundException("");
        
            if (!profile.isConfirmed()) {
                context.buildConstraintViolationWithTemplate("{SignInValidator.notConfirmed}").addConstraintViolation();
            
                return false;
            }
        
            SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(securityService.createUsernamePasswordAuthenticationToken(profile, form.getPassword())));
        } catch (@SuppressWarnings("unused") UsernameNotFoundException | BadCredentialsException e) {
            context.buildConstraintViolationWithTemplate("{SignInValidator.incorrectLoginOrPassword}").addPropertyNode("login").addConstraintViolation();
            context.buildConstraintViolationWithTemplate("").addPropertyNode("password").addConstraintViolation();
            
            return false;
        }
        
        return true;
    }
}