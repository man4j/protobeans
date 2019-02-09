package org.protobeans.i18n.example.service;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class HelloService {
    @Autowired
    private ApplicationContext ctx;
    
    public void sayHello(Locale locale) {
        System.out.println(ctx.getMessage("message.hello", new Object[] {}, locale));
    }
}
