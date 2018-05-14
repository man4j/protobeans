package org.protobeans.mvcsecurity.example;

import org.protobeans.async.annotation.EnableAsync;
import org.protobeans.core.EntryPoint;
import org.protobeans.flyway.annotation.EnableFlyway;
import org.protobeans.freemarker.annotation.EnableFreeMarker;
import org.protobeans.gmail.annotation.EnableGMail;
import org.protobeans.hibernate.annotation.EnableHibernate;
import org.protobeans.i18n.annotation.EnableI18n;
import org.protobeans.mvc.annotation.EnableMvc;
import org.protobeans.mvcsecurity.example.model.UserProfile;
import org.protobeans.mvcsecurity.example.service.InMemoryProfileService;
import org.protobeans.postgresql.annotation.EnablePostgreSql;
import org.protobeans.security.annotation.EnableSecurity;
import org.protobeans.undertow.annotation.EnableUndertow;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.orm.jpa.vendor.Database;

@EnableUndertow
@EnableFreeMarker(devMode = "true")
@EnableMvc(resourcesPath = "static", resourcesUrl = "static")
@EnableSecurity(ignoreUrls = "static", loginUrl = "/signin")
@ComponentScan(basePackageClasses={InMemoryProfileService.class})
@EnableI18n(isCached = "false")
@EnableGMail(user = "s:gmailUser", password = "s:gmailPassword")
@EnableAsync
@EnablePostgreSql(dbHost = "s:dbHost", schema = "s:dbSchema", user = "root", password = "s:dbPassword")
@EnableHibernate(dialect = Database.POSTGRESQL, basePackageClasses = UserProfile.class)
@EnableFlyway(dbHost = "s:dbHost", schema = "s:dbSchema", user = "root", password = "s:dbPassword", waitDb = true)
public class Main {
    public static void main(String[] args) {
        EntryPoint.run(Main.class);
    }
}
