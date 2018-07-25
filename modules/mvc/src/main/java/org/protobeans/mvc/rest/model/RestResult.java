package org.protobeans.mvc.rest.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RestResult<T> {    
    private List<T> errors = new ArrayList<>();
    
    public RestResult(T error) {
        this.errors.add(error);
    }

    public RestResult(List<T> errors) {
        this.errors = errors;
    }
    
    public RestResult() {
        //empty
    }

    @JsonProperty("errors")
    public List<T> getErrors() {
        return errors;
    }
    
    @JsonProperty("success")
    public boolean isSuccess() {
        return getErrors().isEmpty();
    }
}
