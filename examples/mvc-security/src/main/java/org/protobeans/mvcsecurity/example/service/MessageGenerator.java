package org.protobeans.mvcsecurity.example.service;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.AbstractTemplateView;

import freemarker.template.Configuration;

@Service
public class MessageGenerator {
    @Autowired
    private Configuration freemarker;
    
    public String generateEmailSignInMessage(String password, String uuid, String email, RequestContext rc) {
        try {
            StringWriter writer = new StringWriter();
            
            Map<String, Object> emailModel = new HashMap<>();
            
            emailModel.put(AbstractTemplateView.SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE, rc);
            emailModel.put("password", password);
            emailModel.put("email", email);
            emailModel.put("uuid", uuid);
            emailModel.put("baseUrl", ServletUriComponentsBuilder.fromCurrentContextPath().build().toString());
            
            freemarker.getTemplate("email/email_signin.ftlh").process(emailModel, writer);
            
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
