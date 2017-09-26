package org.protobeans.freemarker.example;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;

import org.protobeans.core.EntryPoint;
import org.protobeans.freemarker.annotation.EnableFreeMarker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

@EnableFreeMarker(devMode = "true")
public class Main {
    @Autowired
    freemarker.template.Configuration freemarker;
    
    @EventListener(ContextRefreshedEvent.class)
    void start() throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, TemplateException, IOException {
        StringWriter writer = new StringWriter();
        
//        freemarker.getTemplate("my_template.ftlh").process(Collections.singletonMap("userName", "Mark"), writer);
        
        freemarker.getTemplate("service_description.ftlh").process(Collections.singletonMap("userName", "Mark"), writer);
        
        System.out.println(writer.toString());
    }
    
    public static void main(String[] args) {
        EntryPoint.run(Main.class);
    }
}