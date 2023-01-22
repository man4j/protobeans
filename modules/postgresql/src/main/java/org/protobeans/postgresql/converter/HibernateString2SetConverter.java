package org.protobeans.postgresql.converter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.AttributeConverter;

public class HibernateString2SetConverter implements AttributeConverter<Set<String>, String> {
    @Override
    public String convertToDatabaseColumn(Set<String> attribute) {
        if (attribute.isEmpty()) return null;
        
        return String.join(",", attribute.toArray(new String[] {}));
    }

    @Override
    public Set<String> convertToEntityAttribute(String dbData) {
        if (dbData == null) return new HashSet<>();
        
        return new HashSet<>(Arrays.asList(dbData.split(",")));
    }
}
