package org.protobeans.postgresql.mapper;


import java.io.IOException;
import java.lang.reflect.Type;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.JavaType;
import org.hibernate.type.format.AbstractJsonFormatMapper;

import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.ObjectMapper;

public final class ProtobeansJsonFormatMapper extends AbstractJsonFormatMapper {
    private final ObjectMapper objectMapper;

    public ProtobeansJsonFormatMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public <T> void writeToTarget(T value, JavaType<T> javaType, Object target, WrapperOptions options) {
        objectMapper.writerFor( objectMapper.constructType( javaType.getJavaType() ) ).writeValue( (JsonGenerator) target, value );
    }

    @Override
    public <T> T readFromSource(JavaType<T> javaType, Object source, WrapperOptions options) throws IOException {
        return objectMapper.readValue( (JsonParser) source, objectMapper.constructType( javaType.getJavaType() ) );
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return JsonParser.class.isAssignableFrom( sourceType );
    }

    @Override
    public boolean supportsTargetType(Class<?> targetType) {
        return JsonGenerator.class.isAssignableFrom( targetType );
    }

    @Override
    public <T> T fromString(CharSequence charSequence, Type type) {
        return objectMapper.readValue( charSequence.toString(), objectMapper.constructType( type ) );
    }

    @Override
    public <T> String toString(T value, Type type) {
        return objectMapper.writerFor( objectMapper.constructType( type ) ).writeValueAsString( value );
    }
}

