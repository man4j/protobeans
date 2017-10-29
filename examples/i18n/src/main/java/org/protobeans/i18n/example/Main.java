package org.protobeans.i18n.example;

import org.protobeans.core.EntryPoint;
import org.protobeans.i18n.annotation.EnableI18n;
import org.protobeans.i18n.example.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

@EnableI18n(isCached = "true")
@ComponentScan(basePackageClasses = HelloService.class)
public class Main {
    @Autowired
    private HelloService helloService;
    
    @EventListener(ContextRefreshedEvent.class)
    void start() {
        helloService.sayHello();
    }
    
    public static void main(String[] args) {
        EntryPoint.run(Main.class);
    }
}
