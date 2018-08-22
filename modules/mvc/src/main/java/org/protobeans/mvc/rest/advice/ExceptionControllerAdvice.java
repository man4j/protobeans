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
        logger.warn(ex.getMessage());
        
        return ResponseEntity.badRequest().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(new RestResult(new ArrayList<>(), ex.getMessages()));
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        List<ProtobeansFieldError> fieldErrors = ex.getConstraintViolations().stream()
                                                                             .map(cv -> new ProtobeansFieldError(cv.getPropertyPath().toString(), cv.getMessage()))
                                                                             .collect(Collectors.toList());
        
        logger.warn(fieldErrors.toString());
        
        return ResponseEntity.badRequest().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(new RestResult(fieldErrors));
    }
    
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<ProtobeansFieldError> fieldErrors = new ArrayList<>();
        List<String> globalErrors = new ArrayList<>();
        
        for (FieldError err : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.add(new ProtobeansFieldError(err.getField(), err.getDefaultMessage()));
        }
        
        for (ObjectError err : ex.getBindingResult().getGlobalErrors()) {
            globalErrors.add(err.getDefaultMessage());
        }
        
        logger.warn(fieldErrors.toString());
        logger.warn(globalErrors.toString());
        
        return ResponseEntity.badRequest().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(new RestResult(fieldErrors, globalErrors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleServerException(Exception ex) {
        logger.error("", ex);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(new RestResult(ex.getMessage()));
    }
    
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.warn(ex.getMessage());
        
        return ResponseEntity.badRequest().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(new RestResult(ex.getMessage()));
    }
}
