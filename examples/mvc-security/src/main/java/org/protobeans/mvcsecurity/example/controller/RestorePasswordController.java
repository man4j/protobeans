package org.protobeans.mvcsecurity.example.controller;

import javax.validation.Valid;

import org.protobeans.mvc.controller.advice.RequestContextHolder;
import org.protobeans.mvcsecurity.example.service.EmailService;
import org.protobeans.mvcsecurity.example.service.InMemoryProfileService;
import org.protobeans.mvcsecurity.example.service.MessageGenerator;
import org.protobeans.security.annotation.Anonymous;
import org.protobeans.security.model.AbstractProfile;
import org.protobeans.security.model.RestorePasswordForm;
import org.protobeans.security.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/restore")
@Anonymous("/restore")
public class RestorePasswordController {
    @Autowired
    private InMemoryProfileService profileService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SecurityService securityService;
    
    @Autowired
    private MessageGenerator messageGenerator;
    
    @Autowired
    private RequestContextHolder requestContextHolder;

    @RequestMapping(method = RequestMethod.GET)
    String get(@SuppressWarnings("unused") @ModelAttribute("form") RestorePasswordForm form) {
        return "/restore";
    }

    @RequestMapping(method = RequestMethod.POST)
    String restore(@ModelAttribute("form") @Valid RestorePasswordForm form, BindingResult result) {
        if (result.hasErrors()) return "/restore";

        AbstractProfile p = profileService.getByEmail(form.getEmail());
        
        String newDecryptedPassword = securityService.generatePassword();

        profileService.updatePassword(p, newDecryptedPassword);

        emailService.sendMessage(form.getEmail(), messageGenerator.generateEmailSignInMessage(newDecryptedPassword, p.getConfirmUuid(), requestContextHolder.getRequestContext()));

        return "/check_email";
    }
}
