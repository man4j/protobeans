package org.protobeans.mvc.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FreeMarkerVariables {
    private Map<String, Object> map = new ConcurrentHashMap<>();
    
    public void setVariable(String name, Object value) {
        map.put(name, value);
    }
    
    public Object getVariable(String name) {
        return map.get(name);
    }
    
    public Map<String, Object> getMap() {
        return map;
    }
}
