package org.protobeans.mvc.util;

public class GlobalModelAttribute {
    private String key;
    
    private Object value;

    public GlobalModelAttribute(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }
}
