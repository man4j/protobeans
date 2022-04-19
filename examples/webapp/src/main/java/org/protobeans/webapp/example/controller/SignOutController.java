package org.protobeans.webapp.example.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/logout")
public class SignOutController {
    @Autowired
    private HttpServletRequest servletRequest;
    
    @Autowired
    private HttpServletResponse servletResponse;
    
    @Autowired
    private TokenBasedRememberMeServices rememberMeServices;

    @GetMapping
    String logout() {
        new SecurityContextLogoutHandler().logout(servletRequest, servletResponse, SecurityContextHolder.getContext().getAuthentication());
        
        rememberMeServices.logout(servletRequest, servletResponse, SecurityContextHolder.getContext().getAuthentication());
        
        return "redirect:/signin";
    }
}

