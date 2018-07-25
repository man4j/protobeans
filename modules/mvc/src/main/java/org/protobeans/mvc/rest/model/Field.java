package org.protobeans.mvc.rest.model;

public class Field {
    private String name;
    
    private String value;

    public Field(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
