package org.protobeans.webapp.example.controller;

import org.protobeans.security.annotation.Anonymous;
import org.protobeans.security.service.SecurityService;
import org.protobeans.webapp.example.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ott.OneTimeTokenAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/email_signin")
@Anonymous("/email_signin")
public class EmailSigninController {
    @Autowired UserProfileService profileService;

    @Autowired SecurityService securityService;
    
    @Lazy
    @Autowired AuthenticationManager authenticationManager;
    
    @GetMapping
    String signin(@RequestParam String uuid) {
        try {
            var token = new OneTimeTokenAuthenticationToken(uuid);
            authenticationManager.authenticate(token);

            return "/email_confirmed";
        } catch (BadCredentialsException _) {
            return "/expired_link";
        }
    }
}