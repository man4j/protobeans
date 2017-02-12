package org.protobeans.mvc.example;

import org.protobeans.core.EntryPoint;
import org.protobeans.mvc.annotation.EnableMvc;
import org.protobeans.mvc.example.controller.SimpleController;
import org.protobeans.mvc.example.ws.WsConfig;
import org.protobeans.undertow.annotation.EnableUndertow;
import org.protobeans.webservices.annotation.EnableWebServices;

@EnableUndertow
@EnableMvc(resourcesPath = "static", resourcesUrl = "static", basePackageClasses = SimpleController.class)
@EnableWebServices(mappings = "/ws/*", configClasses = WsConfig.class)
public class MainWs {
    public static void main(String[] args) {
        EntryPoint.run(MainWs.class);
    }
}

