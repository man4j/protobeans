package org.protobeans.social.controller;

import org.protobeans.security.annotation.PermitAll;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@PermitAll("/closeWindow")
@Controller
public class CloseWindowController {
    @GetMapping(path = "/closeWindow")
    String closeWindow() {
        return "/close_window";
    }
}
