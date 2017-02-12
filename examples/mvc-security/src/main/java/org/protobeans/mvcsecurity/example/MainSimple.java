package org.protobeans.mvcsecurity.example;

import org.protobeans.core.EntryPoint;
import org.protobeans.freemarker.annotation.EnableFreeMarker;
import org.protobeans.mvc.annotation.EnableMvc;
import org.protobeans.mvcsecurity.example.controller.MainController;
import org.protobeans.mvcsecurity.example.service.InMemoryProfileService;
import org.protobeans.security.annotation.EnableSecurity;
import org.protobeans.undertow.annotation.EnableUndertow;
import org.springframework.context.annotation.ComponentScan;

@EnableUndertow
@EnableFreeMarker(devMode = "true")
@EnableMvc(resourcesPath = "static", resourcesUrl = "static", basePackageClasses = MainController.class)
@EnableSecurity(ignoreUrls = "static", loginUrl = "/signin")
@ComponentScan(basePackageClasses=InMemoryProfileService.class)
public class MainSimple {
    public static void main(String[] args) {
        EntryPoint.run(MainSimple.class);
    }
}
