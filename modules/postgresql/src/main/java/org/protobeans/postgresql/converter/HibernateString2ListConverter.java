package org.protobeans.postgresql.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jakarta.persistence.AttributeConverter;

public class HibernateString2ListConverter implements AttributeConverter<List<String>, String> {
    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        if (attribute.isEmpty()) return null;
        
        return String.join(",", attribute.toArray(new String[] {}));
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (dbData == null) return new ArrayList<>();
        
        return new ArrayList<>(Arrays.asList(dbData.split(",")));
    }
}
