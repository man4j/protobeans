package org.protobeans.webapp.example.controller;

import java.util.UUID;

import org.protobeans.security.annotation.Anonymous;
import org.protobeans.security.service.SecurityService;
import org.protobeans.webapp.example.entity.UserProfile;
import org.protobeans.webapp.example.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/email_signin")
@Anonymous("/email_signin")
public class EmailSigninController {
    @Autowired
    private UserProfileService profileService;

    @Autowired
    private SecurityService securityService;
    
    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;
    
    @GetMapping
    String signin(@RequestParam String uuid, @RequestParam String email) {
        UserProfile profile = profileService.getByLogin(email);
        
        try {
            if (profile == null) throw new BadCredentialsException("");
            SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(securityService.createUuidAuthenticationToken(profile, uuid)));
                        
            profile.setConfirmUuid(UUID.randomUUID().toString());//Одну и ту же ссылку нельзя использовать дважды
            profile.setConfirmed(true);    
            profileService.update(profile);
            
            return "redirect:/";
        } catch (@SuppressWarnings("unused") BadCredentialsException e) {
            return "/expired_link";
        }
    }
}