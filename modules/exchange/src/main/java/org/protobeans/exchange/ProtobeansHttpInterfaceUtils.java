package org.protobeans.exchange;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.ResourceRegionHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;


public class ProtobeansHttpInterfaceUtils {
    public static ObjectMapper mapper() {
        return JsonMapper.builder().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                                   .configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS, true)
                                   .visibility(PropertyAccessor.FIELD, Visibility.ANY)
                                   .visibility(PropertyAccessor.GETTER, Visibility.NONE)
                                   .visibility(PropertyAccessor.IS_GETTER, Visibility.NONE)
                                   .visibility(PropertyAccessor.SETTER, Visibility.NONE)
                                   .visibility(PropertyAccessor.CREATOR, Visibility.NONE)
                                   .serializationInclusion(Include.NON_NULL)
                                   .build()
                                   .registerModule(new JavaTimeModule())
                                   .registerModule(new ParameterNamesModule())
                                   .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
    
    public static List<HttpMessageConverter<?>> protobeansConverters() {
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        converters.add(new ResourceHttpMessageConverter());
        converters.add(new ResourceRegionHttpMessageConverter());
        converters.add(new AllEncompassingFormHttpMessageConverter());
        converters.add(new MappingJackson2HttpMessageConverter(mapper()));
        
        return converters;
    }
}
