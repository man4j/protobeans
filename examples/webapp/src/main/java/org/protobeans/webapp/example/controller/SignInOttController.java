package org.protobeans.webapp.example.controller;

import org.protobeans.security.annotation.PermitAll;
import org.protobeans.webapp.example.model.SignInOttForm;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(path = "/signin_ott")
@PermitAll("/signin_ott")
@Validated
public class SignInOttController {
    @Autowired TokenBasedRememberMeServices rememberMeServices;
    
    @Autowired HttpServletRequest request;
    
    @Autowired HttpServletResponse response;
    
    @GetMapping
    String prepareForm(@SuppressWarnings("unused") @ModelAttribute("form") SignInOttForm form) {
        return "/signin_ott";
    }
    
    @PostMapping
    String processForm(@SuppressWarnings("unused") @ModelAttribute("form") @Validated SignInOttForm form, BindingResult result) {
        if (!result.hasErrors()) {
            return "redirect:/";
        }
        
        return "/signin_ott";
    }
}
