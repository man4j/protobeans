package org.protobeans.webapp.example.controller;

import org.protobeans.security.model.ChangePasswordForm;
import org.protobeans.security.service.SecurityService;
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
@RequestMapping(path = "/change_password")
@Validated
public class ChangePasswordController {
    @Autowired
    private SecurityService securityService;
    
    @Autowired
    private UserProfileService profileService;
        
    @GetMapping
    String prepareForm(@SuppressWarnings("unused") @ModelAttribute("form") ChangePasswordForm form) {
        return "/change_password";
    }
    
    @PostMapping
    String processForm(@ModelAttribute("form") @Validated ChangePasswordForm form, BindingResult result) {
        if (result.hasErrors()) return "/change_password";
        
        profileService.updatePassword(securityService.getCurrentUser().getUsername(), form.getPassword());
        
        return "/change_success";
    }
}
