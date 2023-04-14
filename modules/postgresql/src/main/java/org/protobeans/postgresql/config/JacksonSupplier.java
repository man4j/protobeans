package org.protobeans.postgresql.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.hypersistence.utils.hibernate.type.util.ObjectMapperSupplier;

public class JacksonSupplier implements ObjectMapperSupplier {
    public static volatile ObjectMapper objectMapper;
    
    @Override
    public ObjectMapper get() {
        return objectMapper != null ? objectMapper : new ObjectMapper();
    }
}
