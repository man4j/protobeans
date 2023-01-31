package org.protobeans.feign.config;

import java.util.List;

import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.feign.annotation.EnableFeign;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
@InjectFrom(EnableFeign.class)
public class FeignConfig {    
    @Bean
    public HttpMessageConverters httpMessageConverters(MappingJackson2HttpMessageConverter jacksonMessageConverter, StringHttpMessageConverter stringMessageConverter) {
        return new HttpMessageConverters(false, List.of(jacksonMessageConverter, stringMessageConverter));
    }
}
