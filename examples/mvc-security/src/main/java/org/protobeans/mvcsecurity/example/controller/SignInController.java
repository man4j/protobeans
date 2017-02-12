package org.protobeans.mvcsecurity.example.controller;

import java.security.Security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.protobeans.mvcsecurity.example.form.SignInForm;
import org.protobeans.security.annotation.Anonymous;
import org.protobeans.security.model.AbstractProfile;
import org.protobeans.security.service.ProfileService;
import org.protobeans.security.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/signin")
@SuppressWarnings("unused")
@Anonymous("/signin")
public class SignInController {
    @Autowired
    private ProfileService profileService;
    
    @Autowired
    private SecurityService securityService;
    
    @Autowired
    private HttpServletRequest request;
    
    @Autowired
    private HttpServletResponse response;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @GetMapping
    String prepareForm(@ModelAttribute("form") SignInForm form) {
        return "/signin";
    }
    
    @PostMapping
    String processForm(@ModelAttribute("form") @Valid SignInForm form, BindingResult result) {
        if (result.hasErrors()) return "/signin";
        
        AbstractProfile profile = profileService.getByEmail(form.getEmail());

        if (profile == null || !passwordEncoder.matches(form.getPassword(), profile.getPassword())) {
            result.rejectValue("email", "signin.incorrectEmailOrPassword", "Неверный логин или пароль");
            result.rejectValue("password", "emptymessage", "");

            return "/signin";
        }
        
        if (!profile.isConfirmed()) {
            result.rejectValue("email", "signin.notConfirmed", "Данный аккаунт не подтвержден по e-mail");
            result.rejectValue("password", "emptymessage", "");

            return "/auth/signin";
        }

        securityService.auth(profile, request, response, form.isRememberMe());
        
        return "redirect:/";
    }
}
