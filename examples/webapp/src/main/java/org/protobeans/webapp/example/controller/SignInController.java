package org.protobeans.webapp.example.controller;

import org.protobeans.security.annotation.PermitAll;
import org.protobeans.security.model.SignInForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(path = "/signin")
@PermitAll("/signin")
@Validated
public class SignInController {
    @Autowired
    private TokenBasedRememberMeServices rememberMeServices;
    
    @Autowired
    private HttpServletRequest request;
    
    @Autowired
    private HttpServletResponse response;
    
    @GetMapping
    String prepareForm(@SuppressWarnings("unused") @ModelAttribute("form") SignInForm form) {
        return "/signin";
    }
    
    @PostMapping
    String processForm(@ModelAttribute("form") @Validated SignInForm form, BindingResult result) {
        if (!result.hasErrors()) {        
            if (form.isRememberMe()) {
                rememberMeServices.onLoginSuccess(request, response, SecurityContextHolder.getContext().getAuthentication());
            }
            
            return "redirect:/";
        }
        
        return "/signin";
    }
}
