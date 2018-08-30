package org.protobeans.core.bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProtobeansContext {
    private Map<String, Object> context = new ConcurrentHashMap<>();
    
    public void putValue(String key, Object value) {
        context.put(key, value);
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getValue(String key) {
        return (T) context.get(key);
    }
}
