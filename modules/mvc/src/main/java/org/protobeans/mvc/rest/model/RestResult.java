package org.protobeans.mvc.rest.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RestResult {    
    private List<ProtobeansFieldError> fieldErrors = new ArrayList<>();
    
    private List<String> globalErrors = new ArrayList<>();
    
    public RestResult(String error) {
        this.globalErrors.add(error);
    }
    
    public RestResult(String fieldName, String fieldError) {
        this.fieldErrors.add(new ProtobeansFieldError(fieldName, fieldError));
    }
    
    public RestResult(List<ProtobeansFieldError> errors) {
        this.fieldErrors = errors;
    }
    
    public RestResult(List<ProtobeansFieldError> fieldErrors, List<String> globalErrors) {
        this.fieldErrors = fieldErrors;
        this.globalErrors = globalErrors;
    }
    
    public RestResult() {
        //empty
    }

    @JsonProperty("fieldErrors")
    public List<ProtobeansFieldError> getFieldErrors() {
        return fieldErrors;
    }
    
    @JsonProperty("globalErrors")
    public List<String> getGlobalErrors() {
        return globalErrors;
    }
    
    @JsonProperty("errors")
    public List<String> getErrors() {
        return globalErrors;
    }
    
    @JsonProperty("success")
    public boolean isSuccess() {
        return getFieldErrors().isEmpty() && getGlobalErrors().isEmpty();
    }
}
