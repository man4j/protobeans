package org.protobeans.hibernate.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladmihalcea.hibernate.type.util.ObjectMapperSupplier;

public class JacksonSupplier implements ObjectMapperSupplier {
    public static ObjectMapper objectMapper;
    
    @Override
    public ObjectMapper get() {
        return objectMapper;
    }
}
