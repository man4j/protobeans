package org.protobeans.mvc.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleController {
    @Autowired
    private ApplicationContext ctx;
    
    @RequestMapping("/hello")
    String getResult() {
        return "hello";
    }
}
