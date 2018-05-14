package org.protobeans.security.advice;

import javax.annotation.PostConstruct;

import org.protobeans.security.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

@ControllerAdvice
public class SecurityControllerAdvice {
    @Autowired
    private SecurityService securityService;
    
    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @PostConstruct
    public void init() {
        //нужно для того, чтобы при редиректах дефолтная модель не добавлялась в параметры урла
        //причём выполнить данную настройку лучше здесь, т.к. если это вынести в класс
        //аннотированный @Configuration, то возникают какие-то баги
        requestMappingHandlerAdapter.setIgnoreDefaultModelOnRedirect(true);
    }
    
    @ModelAttribute
    User getCurrentUser() {
        return securityService.getCurrentUser();
    }
    
    @ModelAttribute
    SecurityService getSecurityService() {
        return securityService;
    }
}
