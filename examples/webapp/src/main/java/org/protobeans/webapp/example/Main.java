package org.protobeans.webapp.example;

import org.protobeans.feign.annotation.EnableFeign;
import org.protobeans.freemarker.annotation.EnableFreeMarker;
import org.protobeans.mail.annotation.EnableMail;
import org.protobeans.mvc.annotation.EnableMvc;
import org.protobeans.mvc.annotation.EnableSwagger3;
import org.protobeans.postgresql.annotation.EnablePostgreSql;
import org.protobeans.security.annotation.EnableSecurity;
import org.protobeans.undertow.MvcEntryPoint;
import org.protobeans.undertow.annotation.EnableUndertow;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@EnableUndertow(port = "8787")
@EnableFreeMarker(devMode = "true")
@EnableMvc
@EnableSecurity
@EnableFeign
@EnableMail(host = "smtp.sendgrid.net", user = "apikey", password = "${emailPassword}")
@EnablePostgreSql(showSql = "false", schema = "demo", dbHost = "127.0.0.1", dbPort = "5432", user = "postgres", password = "PassWord111", basePackages = {"org.protobeans.webapp.example"}, migrationsPath = "migrations")
@ComponentScan(basePackages = "org.protobeans.webapp.example")
@EnableSwagger3
@PropertySource("classpath:application.properties")
public class Main {
    @SuppressWarnings("resource")
    public static void main(String[] args) {
        MvcEntryPoint.run(Main.class);
    }
}
