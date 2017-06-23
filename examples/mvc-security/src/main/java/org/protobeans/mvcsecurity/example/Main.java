package org.protobeans.mvcsecurity.example;

import java.nio.charset.StandardCharsets;

import org.protobeans.async.annotation.EnableAsync;
import org.protobeans.core.EntryPoint;
import org.protobeans.flyway.annotation.EnableFlyway;
import org.protobeans.freemarker.annotation.EnableFreeMarker;
import org.protobeans.gmail.annotation.EnableGMail;
import org.protobeans.i18n.annotation.EnableI18n;
import org.protobeans.mvc.annotation.EnableMvc;
import org.protobeans.mvc.util.MessageConvertersBean;
import org.protobeans.mvcsecurity.example.controller.MainController;
import org.protobeans.mvcsecurity.example.service.InMemoryProfileService;
import org.protobeans.mysql.annotation.EnableMySql;
import org.protobeans.security.annotation.EnableSecurity;
import org.protobeans.social.annotation.EnableFacebook;
import org.protobeans.undertow.annotation.EnableUndertow;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

@EnableUndertow
@EnableFreeMarker(devMode = "true")
@EnableMvc(resourcesPath = "static", resourcesUrl = "static", basePackageClasses = MainController.class)
@EnableSecurity(ignoreUrls = "static", loginUrl = "/signin")
@ComponentScan(basePackageClasses={InMemoryProfileService.class})
@EnableI18n(isCached = "false")
@EnableGMail(user = "s:gmailUser", password = "s:gmailPassword")
@EnableAsync
@EnableMySql(dbUrl = "s:dbUrl", schema = "s:dbSchema", user = "root", password = "s:dbPassword")
@EnableFlyway(dbUrl = "s:dbUrl", schema = "s:dbSchema", user = "root", password = "s:dbPassword", waitDb = true)
@EnableFacebook(appId = "1116746028470918", appSecret = "s:facebookSecret")
public class Main {
    @Bean
    public ObjectMapper mapper() {
        return new ObjectMapper().setVisibility(PropertyAccessor.FIELD, Visibility.ANY)
                                 .setVisibility(PropertyAccessor.GETTER, Visibility.NONE)
                                 .setVisibility(PropertyAccessor.IS_GETTER, Visibility.NONE)
                                 .setVisibility(PropertyAccessor.SETTER, Visibility.NONE)
                                 .setVisibility(PropertyAccessor.CREATOR, Visibility.NONE)
                                 .setSerializationInclusion(Include.NON_EMPTY);
    }
    
    @Bean
    MessageConvertersBean messageConvertersBean() {
        return new MessageConvertersBean(new MappingJackson2HttpMessageConverter(mapper()))
            .addConverter(new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }
    
    public static void main(String[] args) {
        EntryPoint.run(Main.class);
    }
}
