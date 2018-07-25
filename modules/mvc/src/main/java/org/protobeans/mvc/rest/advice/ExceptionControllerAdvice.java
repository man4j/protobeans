package org.protobeans.mvc.rest.advice;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.protobeans.mvc.rest.exception.BusinessException;
import org.protobeans.mvc.rest.model.ProtobeansFieldError;
import org.protobeans.mvc.rest.model.RestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);
    
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(BusinessException ex) {
        return ResponseEntity.badRequest().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(new RestResult(ex.getMessage()));
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        List<ProtobeansFieldError> errorFields = ex.getConstraintViolations().stream()
                                                              .map(cv -> new ProtobeansFieldError(cv.getPropertyPath().toString(), cv.getMessage()))
                                                              .collect(Collectors.toList());
        
        return ResponseEntity.badRequest().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(new RestResult(errorFields));
    }
    
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        
        List<ProtobeansFieldError> errorFields = new ArrayList<>();

        int counter = 1;
        
        for (ObjectError err : globalErrors) {
            errorFields.add(new ProtobeansFieldError(err.getObjectName() + "_" + counter++, err.getDefaultMessage()));
        }
        
        for (FieldError err : fieldErrors) {
            errorFields.add(new ProtobeansFieldError(err.getField(), err.getDefaultMessage()));
        }
        
        return ResponseEntity.badRequest().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(new RestResult(errorFields));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleServerException(Exception ex) {
        logger.error("", ex);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(new RestResult(ex.getMessage()));
    }
    
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity.badRequest().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(new RestResult(ex.getMessage()));
    }
}
