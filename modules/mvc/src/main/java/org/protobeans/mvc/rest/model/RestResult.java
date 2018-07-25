package org.protobeans.mvc.rest.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RestResult {    
    private List<ProtobeansFieldError> errors = new ArrayList<>();
    
    public RestResult(String error) {
        this.errors.add(new ProtobeansFieldError("default", error));
    }

    public RestResult(List<ProtobeansFieldError> errors) {
        this.errors = errors;
    }
    
    public RestResult() {
        //empty
    }

    @JsonProperty("errors")
    public List<ProtobeansFieldError> getErrors() {
        return errors;
    }
    
    @JsonProperty("success")
    public boolean isSuccess() {
        return getErrors().isEmpty();
    }
}
