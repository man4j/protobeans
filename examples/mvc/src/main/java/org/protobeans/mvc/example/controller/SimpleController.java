package org.protobeans.mvc.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleController {
    @GetMapping("/echo")
    String getResult(String param) {
        return "Hello, " + param + "!";
    }
}
