package org.protobeans.feign.exception;

public class FeignJsonBodyException extends Exception {
    public FeignJsonBodyException(String json) {
        super(json);
    }
    
    public String getJson() {
        return getMessage();
    }
}
