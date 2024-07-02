package org.protobeans.webapp.example.controller;

import org.protobeans.security.annotation.AdminRole;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.annotation.PostConstruct;

@Controller
@AdminRole
public class AdminController {
    
    @PostConstruct
    void init() {
        System.out.println("123");
    }
    
    @GetMapping(path = "/admin")
    String getResult() {
        return "/admin";
    }
}
