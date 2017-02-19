package org.protobeans.mvcsecurity.example.controller;

import javax.validation.Valid;

import org.protobeans.mvcsecurity.example.model.MySignUpForm;
import org.protobeans.mvcsecurity.example.model.UserProfile;
import org.protobeans.mvcsecurity.example.service.EmailService;
import org.protobeans.mvcsecurity.example.service.InMemoryProfileService;
import org.protobeans.mvcsecurity.example.service.MessageGenerator;
import org.protobeans.security.annotation.Anonymous;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/signup")
@Anonymous("/signup")
public class SignUpController {
    @Autowired
    private InMemoryProfileService profileService;
    
    @Autowired
    private ShaPasswordEncoder passwordEncoder;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private MessageGenerator messageGenerator;
    
    @GetMapping
    String prepareForm(@SuppressWarnings("unused") @ModelAttribute("form") MySignUpForm form) {
        return "/signup";
    }
    
    @PostMapping
    String processForm(@ModelAttribute("form") @Valid MySignUpForm form, BindingResult result) {
        if (result.hasErrors()) return "/signup";
        
        UserProfile p = profileService.create(form.getEmail(), passwordEncoder.encodePassword(form.getPassword(), form.getEmail()), form.getUserName());
        
        emailService.sendMessage(form.getEmail(), messageGenerator.generateEmailSignInMessage(form.getPassword(), p.getConfirmUuid()));

        return "/check_email";
    }
}
