package org.protobeans.exchange.exception;

import org.protobeans.exchange.model.RestResult;
import org.springframework.http.HttpStatusCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RestResultException extends RuntimeException {
    private HttpStatusCode httpStatus;
    
    private RestResult restResult;
}
