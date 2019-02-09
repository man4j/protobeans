package org.protobeans.webapp.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping(path = "/")
    String getResult() {
        return "/index";
    }

//    @GetMapping(path = "/message/{id}")
//    @PreAuthorize("hasPermission(#id, 'read')")
//    @ResponseBody
//    String getMessage(@PathVariable("id") String id) {
//        return "hello! id: " + id;
//    }
}
