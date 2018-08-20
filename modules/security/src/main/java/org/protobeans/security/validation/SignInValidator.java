package org.protobeans.security.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.protobeans.security.model.AbstractProfile;
import org.protobeans.security.model.SignInForm;
import org.protobeans.security.service.ProfileService;
import org.protobeans.security.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class SignInValidator implements ConstraintValidator<SignIn, SignInForm> {
    @Autowired
    private ProfileService profileService;
    
    @Autowired
    private SecurityService securityService;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Override
    public void initialize(SignIn constraintAnnotation) {
        //empty
    }

    @Override
    public boolean isValid(SignInForm form, ConstraintValidatorContext context) {
        if (form.getId() == null || form.getId().trim().isEmpty()) return true;
        if (form.getPassword() == null || form.getPassword().trim().isEmpty()) return true;
        
        context.disableDefaultConstraintViolation();

        AbstractProfile profile = profileService.getByLogin(form.getId());
        
        try {
            if (profile == null) throw new UsernameNotFoundException("");
        
            if (!profile.isConfirmed()) {
                context.buildConstraintViolationWithTemplate("{SignInValidator.notConfirmed}").addConstraintViolation();
            
                return false;
            }
        
            SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(securityService.createUsernamePasswordAuthenticationToken(profile, form.getPassword())));
        } catch (@SuppressWarnings("unused") UsernameNotFoundException | BadCredentialsException e) {
//            context.buildConstraintViolationWithTemplate("{SignInValidator.incorrectEmailOrPassword}").addPropertyNode("login").addConstraintViolation();
            context.buildConstraintViolationWithTemplate("").addPropertyNode("password").addConstraintViolation();
            context.buildConstraintViolationWithTemplate("{SignInValidator.incorrectLoginOrPassword}").addPropertyNode("id").addConstraintViolation();
            
            return false;
        }
        
        return true;
    }
}