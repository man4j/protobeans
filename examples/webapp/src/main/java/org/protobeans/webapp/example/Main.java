package org.protobeans.webapp.example;

import org.protobeans.freemarker.annotation.EnableFreeMarker;
import org.protobeans.i18n.annotation.EnableI18n;
import org.protobeans.mail.annotation.EnableMail;
import org.protobeans.mvc.MvcEntryPoint;
import org.protobeans.mvc.annotation.EnableMvc;
import org.protobeans.postgresql.annotation.EnablePostgreSql;
import org.protobeans.security.annotation.EnableSecurity;
import org.protobeans.undertow.annotation.EnableUndertow;
import org.protobeans.webapp.example.controller.MainController;
import org.protobeans.webapp.example.repository.UserProfileRepository;
import org.protobeans.webapp.example.service.UserProfileService;
import org.springframework.context.annotation.ComponentScan;

@EnableUndertow(port = "8787")
@EnableFreeMarker(devMode = "true")
@EnableMvc(resourcesPath = "static", resourcesUrl = "static")
@EnableSecurity(ignoreUrls = "static", loginUrl = "/signin")
@EnableI18n(isCached = "false")
@EnableMail(host = "smtp.sendgrid.net", user = "apikey", password = "${emailPassword}")
@EnablePostgreSql(schema = "postgres", dbHost = "${postgresIp}", dbPort = "${postgresPort}", dialect = "org.hibernate.dialect.PostgreSQL10Dialect", user = "postgres", password = "postgres", basePackages = {"org.protobeans.webapp.example"}, migrationsPath = "migrations")
@ComponentScan(basePackageClasses={MainController.class, UserProfileService.class, UserProfileRepository.class})
public class Main {
    @SuppressWarnings("resource")
    public static void main(String[] args) {
        MvcEntryPoint.run(Main.class);
    }
}
