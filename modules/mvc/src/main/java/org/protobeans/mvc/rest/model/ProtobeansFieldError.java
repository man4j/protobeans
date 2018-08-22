package org.protobeans.mvc.rest.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProtobeansFieldError {
    private String fieldName;
    
    private String fieldError;

    @JsonCreator
    public ProtobeansFieldError(@JsonProperty("fieldName") String fieldName, @JsonProperty("fieldError") String fieldError) {
        this.fieldName = fieldName;
        this.fieldError = fieldError;
    }

    public String getName() {
        return fieldName;
    }

    public String getValue() {
        return fieldError;
    }
    
    @Override
    public String toString() {
        return fieldName + " : " + fieldError;
    }
}
