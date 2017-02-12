package org.protobeans.mvc.example;

import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;

import javax.servlet.Filter;

import org.protobeans.core.EntryPoint;
import org.protobeans.i18n.annotation.EnableI18n;
import org.protobeans.mvc.annotation.EnableMvc;
import org.protobeans.mvc.example.controller.SimpleController;
import org.protobeans.mvc.util.MessageConvertersBean;
import org.protobeans.undertow.annotation.EnableUndertow;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

@EnableUndertow
@EnableMvc(resourcesPath = "static", resourcesUrl = "static", basePackageClasses = SimpleController.class)
@EnableI18n(isCached = "true")
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
    
    @Bean
    Filter characterEncodingFilter() {
        return new CharacterEncodingFilter(StandardCharsets.UTF_8.displayName(), true, true);
    }
    
    public static void main(String[] args) {
        EntryPoint.run(Main.class);
    }
}
