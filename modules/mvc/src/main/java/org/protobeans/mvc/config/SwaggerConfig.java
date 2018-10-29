package org.protobeans.mvc.config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.mvc.annotation.EnableSwagger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.annotations.Api;
import io.swagger.annotations.Info;
import io.swagger.annotations.Tag;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@InjectFrom(EnableSwagger.class)
@EnableSwagger2
@Configuration
public class SwaggerConfig {
    private Info info;
    
    private Tag[] tags;
    
    @Bean
    public Docket api() {
        List<springfox.documentation.service.Tag> serviceTags = Arrays.stream(tags).map(a -> new springfox.documentation.service.Tag(a.name(), a.description())).collect(Collectors.toList());
        
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .apiInfo(new ApiInfo(info.title(), info.description(), info.version(), info.termsOfService(), info.contact().name(), info.license().name(), info.license().url()))
                .tags(serviceTags.get(0), serviceTags.toArray(new springfox.documentation.service.Tag[] {}))
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))              
                .paths(PathSelectors.any())
                .build();                                           
    }
}
