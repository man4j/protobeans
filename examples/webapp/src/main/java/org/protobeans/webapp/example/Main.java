package org.protobeans.webapp.example;

import java.nio.charset.StandardCharsets;

import org.protobeans.async.annotation.EnableAsync;
import org.protobeans.flyway.annotation.EnableFlyway;
import org.protobeans.freemarker.annotation.EnableFreeMarker;
import org.protobeans.hibernate.annotation.EnableHibernate;
import org.protobeans.i18n.annotation.EnableI18n;
import org.protobeans.mail.annotation.EnableMail;
import org.protobeans.mvc.MvcEntryPoint;
import org.protobeans.mvc.annotation.EnableMvc;
import org.protobeans.postgresql.annotation.EnablePostgreSql;
import org.protobeans.security.annotation.EnableSecurity;
import org.protobeans.undertow.annotation.EnableUndertow;
import org.protobeans.webapp.example.controller.MainController;
import org.protobeans.webapp.example.dao.UserProfileDao;
import org.protobeans.webapp.example.service.UserProfileService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

@EnableUndertow
@EnableFreeMarker(devMode = "true")
@EnableMvc(resourcesPath = "static", resourcesUrl = "static")
@EnableSecurity(ignoreUrls = "static", loginUrl = "/signin")
@EnableI18n(isCached = "false")
@EnableMail(host = "smtp.sendgrid.net", user = "e:mailUser", password = "e:mailPassword")
@EnableAsync
@EnablePostgreSql(dbHost = "e:dbHost", dbPort = "5432", schema = "postgres", user = "postgres", password = "postgres")
@EnableFlyway
@EnableHibernate(dialect = Database.POSTGRESQL, basePackages = "org.protobeans.webapp.example.model")
@ComponentScan(basePackageClasses={MainController.class, UserProfileService.class, UserProfileDao.class})
public class Main {
    @Bean
    public SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> securityConfigurer() {
        return new SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {
            @Override
            public void configure(HttpSecurity builder) throws Exception {
                builder.addFilterBefore(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true, true), ChannelProcessingFilter.class);
            }
        };
    }
    
    public static void main(String[] args) {
        MvcEntryPoint.run(Main.class);
    }
}
