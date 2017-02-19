package org.protobeans.mvcsecurity.example.controller;

import javax.validation.Valid;

import org.protobeans.security.annotation.Anonymous;
import org.protobeans.security.model.AbstractProfile;
import org.protobeans.security.model.SignInForm;
import org.protobeans.security.service.ProfileService;
import org.protobeans.security.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/signin")
@Anonymous("/signin")
public class SignInController {
    @Autowired
    private ProfileService profileService;
    
    @Autowired
    private SecurityService securityService;
    
    @GetMapping
    String prepareForm(@SuppressWarnings("unused") @ModelAttribute("form") SignInForm form) {
        return "/signin";
    }
    
    @PostMapping
    String processForm(@ModelAttribute("form") @Valid SignInForm form, BindingResult result) {
        if (result.hasErrors()) return "/signin";
        
        AbstractProfile profile = profileService.getByEmail(form.getEmail());

        securityService.auth(profile, form.isRememberMe());
        
        return "redirect:/";
    }
}
