package org.protobeans.mvcsecurity.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping(path = "/")
    String getResult() {
        return "/index";
    }
}
