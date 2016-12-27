package org.protobeans.i18n.example;

import java.util.Locale;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.protobeans.core.EntryPoint;
import org.protobeans.i18n.annotation.EnableI18n;

@EnableI18n(isCached = "true")
public class Main {
    @Bean
    HelloService helloService() {
        return new HelloService();
    }
    
    @Autowired
    HelloService helloService;
    
    @EventListener(ContextRefreshedEvent.class)
    void start() {
        helloService.sayHello();
    }
    
    public static void main(String[] args) {
        EntryPoint.run(Main.class);
    }
}

class HelloService {
    @Autowired
    ApplicationContext ctx;
    
    void sayHello() {
        System.out.println(ctx.getMessage("message.hello", new Object[] {}, new Locale("ru", "RU")));
    }
}
