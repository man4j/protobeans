package org.protobeans.mvc.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleController {
    @RequestMapping("/hello")
    String getResult() {
        return "hello";
    }
}
