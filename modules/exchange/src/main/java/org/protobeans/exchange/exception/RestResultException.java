package org.protobeans.exchange.exception;

import org.protobeans.exchange.model.RestResult;
import org.springframework.http.HttpStatusCode;

public class RestResultException extends RuntimeException {
    private HttpStatusCode httpStatus;
    
    private RestResult restResult;
    
    public RestResultException(HttpStatusCode httpStatus, RestResult restResult) {
        this.httpStatus = httpStatus;
        this.restResult = restResult;
    }

    public RestResult getRestResult() {
        return restResult;
    }
    
    public HttpStatusCode getHttpStatus() {
        return httpStatus;
    }
}
