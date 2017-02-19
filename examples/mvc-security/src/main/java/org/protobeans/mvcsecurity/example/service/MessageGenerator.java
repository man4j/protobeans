package org.protobeans.mvcsecurity.example.service;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import freemarker.template.Configuration;

@Service
public class MessageGenerator {
    @Autowired
    private Configuration freemarker;
    
    public String generateEmailSignInMessage(String password, String uuid) {
        try {
            StringWriter writer = new StringWriter();
            
            Map<String, String> emailModel = new HashMap<>();
            emailModel.put("password", password);
            emailModel.put("uuid", uuid);
            emailModel.put("baseUrl", ServletUriComponentsBuilder.fromCurrentContextPath().build().toString());
            
            freemarker.getTemplate("email/email_signin.ftlh").process(emailModel, writer);
            
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
