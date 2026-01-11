package org.protobeans.mvc.rest.advice;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.protobeans.exchange.exception.BusinessException;
import org.protobeans.exchange.exception.NotFoundException;
import org.protobeans.exchange.exception.RestResultException;
import org.protobeans.exchange.model.ProtobeansFieldError;
import org.protobeans.exchange.model.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice(annotations = RestController.class)
@Order(100)
public class RestResultControllerAdvice extends ResponseEntityExceptionHandler {
    @Autowired MessageSource messageSource;
    
    @Autowired LocaleResolver localeResolver;
    
    @Autowired HttpServletRequest request;
    
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(BusinessException ex) {
        List<String> messages = ex.getMessagesWithParams().stream()
                                                          .map(entry -> messageSource.getMessage(entry.getKey(), 
                                                                                                         entry.getValue(), 
                                                                                                         entry.getKey(), 
                                                                                                         localeResolver.resolveLocale(request)))
                                                          .collect(Collectors.toList());

        logger.warn(String.join("\n", messages));
        
        return ResponseEntity.badRequest()
                             .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                             .body(new RestResult(new ArrayList<>(), messages));
    }
    
    @ExceptionHandler(RestResultException.class)
    public ResponseEntity<Object> handleRestResultException(RestResultException ex) {
        logger.warn(ex.getRestResult().getFieldErrors().toString());
        logger.warn(ex.getRestResult().getGlobalErrors().toString());
        
        return ResponseEntity.status(ex.getHttpStatus())
                             .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                             .body(ex.getRestResult());
    }
    
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleBusinessException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                             .body(new RestResult((ex.getMessage() == null || ex.getMessage().isBlank()) ? "Not found" : ex.getMessage()));
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        var fieldErrors = ex.getConstraintViolations()
                            .stream()
                            .map(cv -> new ProtobeansFieldError(cv.getPropertyPath().toString(), cv.getMessage()))
                            .collect(Collectors.toList());
        
        logger.warn(fieldErrors.toString());
        
        return ResponseEntity.badRequest()
                             .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                             .body(new RestResult(fieldErrors));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleServerException(Exception ex) {
        logger.error("", ex);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                             .body(new RestResult(ex.getMessage()));
    }
    
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest webRequest) {
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
        
        return ResponseEntity.badRequest()
                             .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                             .body(new RestResult(fieldErrors, globalErrors));
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest webRequest) {
        logger.warn(ex.getMessage());
        
        return ResponseEntity.badRequest()
                             .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                             .body(new RestResult(ex.getMessage()));
    }
}
