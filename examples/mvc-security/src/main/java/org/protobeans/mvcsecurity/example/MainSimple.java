package org.protobeans.mvcsecurity.example;

import org.protobeans.async.annotation.EnableAsync;
import org.protobeans.core.EntryPoint;
import org.protobeans.freemarker.annotation.EnableFreeMarker;
import org.protobeans.gmail.annotation.EnableGMail;
import org.protobeans.i18n.annotation.EnableI18n;
import org.protobeans.mvc.annotation.EnableMvc;
import org.protobeans.mvcsecurity.example.controller.MainController;
import org.protobeans.mvcsecurity.example.service.InMemoryProfileService;
import org.protobeans.mysql.annotation.EnableMySql;
import org.protobeans.security.annotation.EnableSecurity;
import org.protobeans.social.annotation.EnableFacebook;
import org.protobeans.undertow.annotation.EnableUndertow;
import org.springframework.context.annotation.ComponentScan;

@EnableUndertow
@EnableFreeMarker(devMode = "true")
@EnableMvc(resourcesPath = "static", resourcesUrl = "static", basePackageClasses = MainController.class)
@EnableSecurity(ignoreUrls = "static", loginUrl = "/signin")
@ComponentScan(basePackageClasses={InMemoryProfileService.class})
@EnableI18n(isCached = "false")
@EnableGMail(user = "s:gmailUser", password = "s:gmailPassword")
@EnableAsync
@EnableMySql(dbUrl = "jdbc:mysql://localhost:3306/dev_db", user = "root", password = "s:password")
@EnableFacebook(appId = "1116746028470918", appSecret = "s:appSecret")
public class MainSimple {
    public static void main(String[] args) {
        EntryPoint.run(MainSimple.class);
    }
}
