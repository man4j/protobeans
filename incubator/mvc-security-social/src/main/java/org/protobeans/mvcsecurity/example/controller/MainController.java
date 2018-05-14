package org.protobeans.mvcsecurity.example.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
    @GetMapping(path = "/")
    String getResult() {
        return "/index";
    }

    @GetMapping(path = "/message/{id}")
    @PreAuthorize("hasPermission(#id, 'read')")
    @ResponseBody
    String getMessage(@PathVariable("id") String id) {
        return "hello! id: " + id;
    }
}
