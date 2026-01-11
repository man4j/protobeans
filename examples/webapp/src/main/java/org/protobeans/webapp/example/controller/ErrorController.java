package org.protobeans.webapp.example.controller;

import org.protobeans.security.annotation.Anonymous;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/error")
@Anonymous("/error")
public class ErrorController {
    @RequestMapping(method = RequestMethod.POST)
    String showErrorPage() {
        return "/error";
    }
}
