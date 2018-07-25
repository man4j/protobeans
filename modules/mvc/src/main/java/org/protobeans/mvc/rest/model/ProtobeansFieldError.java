package org.protobeans.mvc.rest.model;

public class ProtobeansFieldError {
    private String fieldName;
    
    private String fieldError;

    public ProtobeansFieldError(String fieldName, String fieldError) {
        this.fieldName = fieldName;
        this.fieldError = fieldError;
    }

    public String getName() {
        return fieldName;
    }

    public String getValue() {
        return fieldError;
    }
}
