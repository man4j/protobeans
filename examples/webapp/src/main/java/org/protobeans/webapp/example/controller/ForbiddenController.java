package org.protobeans.webapp.example.controller;

import org.protobeans.security.annotation.PermitAll;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/forbidden")
@PermitAll("/forbidden")
@Validated
public class ForbiddenController {
    @GetMapping
    String prepareForm() {
        return "/forbidden";
    }
}
