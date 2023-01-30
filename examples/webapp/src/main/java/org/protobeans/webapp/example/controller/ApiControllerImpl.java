package org.protobeans.webapp.example.controller;

import org.protobeans.webapp.example.api.ApiController;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiControllerImpl implements ApiController {
    @Override
    public String getVersion() throws Exception {
        return "1.0";
    }
}
