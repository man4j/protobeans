package org.protobeans.mvc.example;

import org.protobeans.mvc.MvcEntryPoint;
import org.protobeans.mvc.annotation.EnableMvc;
import org.protobeans.mvc.example.controller.SimpleController;
import org.protobeans.undertow.annotation.EnableUndertow;
import org.springframework.context.annotation.ComponentScan;

@EnableUndertow
@EnableMvc(resourcesPath = "static", resourcesUrl = "static")
@ComponentScan(basePackageClasses=SimpleController.class)
public class Main {
    public static void main(String[] args) {
        MvcEntryPoint.run(Main.class);
    }
}
