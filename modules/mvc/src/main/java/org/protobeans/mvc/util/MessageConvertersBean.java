package org.protobeans.mvc.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.converter.HttpMessageConverter;

public class MessageConvertersBean {
    private List<HttpMessageConverter<?>> converters = new ArrayList<>();
    
    public MessageConvertersBean(HttpMessageConverter<?> converter) {
        converters.add(converter);
    }

    public MessageConvertersBean addConverter(HttpMessageConverter<?> converter) {
        converters.add(converter);
        
        return this;
    }
    
    public List<HttpMessageConverter<?>> getConverters() {
        return converters;
    }
}
