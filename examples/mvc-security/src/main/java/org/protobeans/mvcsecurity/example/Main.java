package org.protobeans.mvcsecurity.example;

import org.protobeans.async.annotation.EnableAsync;
import org.protobeans.flyway.annotation.EnableFlyway;
import org.protobeans.freemarker.annotation.EnableFreeMarker;
import org.protobeans.gmail.annotation.EnableGMail;
import org.protobeans.hibernate.annotation.EnableHibernate;
import org.protobeans.i18n.annotation.EnableI18n;
import org.protobeans.mvc.MvcEntryPoint;
import org.protobeans.mvc.annotation.EnableMvc;
import org.protobeans.mvcsecurity.example.controller.MainController;
import org.protobeans.mvcsecurity.example.dao.UserProfileDao;
import org.protobeans.mvcsecurity.example.model.UserProfile;
import org.protobeans.mvcsecurity.example.service.UserProfileService;
import org.protobeans.postgresql.annotation.EnablePostgreSql;
import org.protobeans.security.annotation.EnableSecurity;
import org.protobeans.undertow.annotation.EnableUndertow;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.orm.jpa.vendor.Database;

@EnableUndertow
@EnableFreeMarker(devMode = "true")
@EnableMvc(resourcesPath = "static", resourcesUrl = "static")
@EnableSecurity(ignoreUrls = "static", loginUrl = "/signin")
@EnableI18n(isCached = "false")
@EnableGMail(user = "e:gmailUser", password = "e:gmailPassword")
@EnableAsync
@EnablePostgreSql(dbHost = "e:dbHost", dbPort = "26257", schema = "testdb", user = "root", password = "")
@EnableFlyway(dbProtocol = "jdbc:postgresql://", dbHost = "e:dbHost", dbPort = "26257", schema = "testdb", user = "root", password = "", waitDb = true)
@EnableHibernate(dialect = Database.POSTGRESQL, basePackageClasses = UserProfile.class)
@ComponentScan(basePackageClasses={MainController.class, UserProfileService.class, UserProfileDao.class})
public class Main {
    public static void main(String[] args) {
        MvcEntryPoint.run(Main.class);
    }
}
