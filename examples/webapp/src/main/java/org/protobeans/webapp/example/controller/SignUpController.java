package org.protobeans.webapp.example.controller;

import org.protobeans.mvc.controller.advice.RequestContextHolder;
import org.protobeans.security.annotation.Anonymous;
import org.protobeans.webapp.example.entity.UserProfile;
import org.protobeans.webapp.example.model.MySignUpForm;
import org.protobeans.webapp.example.service.EmailService;
import org.protobeans.webapp.example.service.MessageGenerator;
import org.protobeans.webapp.example.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/signup")
@Anonymous("/signup")
@Validated
public class SignUpController {
    @Autowired
    private UserProfileService profileService;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private MessageGenerator messageGenerator;
    
    @Autowired
    private RequestContextHolder requestContextHolder;
    
    @GetMapping
    String prepareForm(@SuppressWarnings("unused") @ModelAttribute("form") MySignUpForm form) {
        return "/signup";
    }
    
    @PostMapping
    String processForm(@ModelAttribute("form") @Validated MySignUpForm form, BindingResult result) {
        if (result.hasErrors()) return "/signup";
        
        UserProfile p = profileService.createAndSave(form.getEmail(), form.getPassword(), form.getUserName(), false);
        
        emailService.sendMessage(form.getEmail(), "Sign Up", messageGenerator.generateEmailSignInMessage(form.getPassword(), p.getConfirmUuid(), p.getEmail(), requestContextHolder.getRequestContext()));

        return "/check_email";
    }
}
