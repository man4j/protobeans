package org.protobeans.mvcsecurity.example.controller;

import org.protobeans.security.annotation.AdminRole;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AdminRole
public class AdminController {
    @GetMapping(path = "/admin")
    String getResult() {
        return "/admin";
    }
}
