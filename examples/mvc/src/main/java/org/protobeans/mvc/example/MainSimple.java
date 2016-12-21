package org.protobeans.mvc.example;

import org.protobeans.core.EntryPoint;
import org.protobeans.mvc.annotation.EnableMvc;
import org.protobeans.mvc.example.controller.SimpleController;
import org.protobeans.undertow.annotation.EnableUndertow;

@EnableUndertow
@EnableMvc(resourcesPath = "static", resourcesUrl = "static", basePackageClasses = SimpleController.class)
public class MainSimple {
    public static void main(String[] args) {
        EntryPoint.run(MainSimple.class);
    }
}
