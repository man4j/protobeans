package org.protobeans.webapp.example.controller;

import org.protobeans.mvc.controller.advice.RequestContextHolder;
import org.protobeans.security.annotation.Anonymous;
import org.protobeans.security.model.RestorePasswordForm;
import org.protobeans.security.service.SecurityService;
import org.protobeans.webapp.example.entity.UserProfile;
import org.protobeans.webapp.example.service.EmailService;
import org.protobeans.webapp.example.service.MessageGenerator;
import org.protobeans.webapp.example.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/restore")
@Anonymous(mvcPattern = "/restore")
@Validated
public class RestorePasswordController {
    @Autowired
    private UserProfileService profileService;

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
    String restore(@ModelAttribute("form") @Validated RestorePasswordForm form, BindingResult result) {
        if (result.hasErrors()) return "/restore";

        UserProfile p = profileService.getById(form.getEmail());
        
        String newDecryptedPassword = securityService.generatePassword();

        profileService.updatePassword(p.getEmail(), newDecryptedPassword);

        emailService.sendMessage(form.getEmail(), "Restore password", messageGenerator.generateEmailSignInMessage(newDecryptedPassword, p.getConfirmUuid(), p.getEmail(), requestContextHolder.getRequestContext()));

        return "/check_email";
    }
}
