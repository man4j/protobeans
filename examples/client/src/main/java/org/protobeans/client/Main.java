package org.protobeans.client;

import org.protobeans.client.model.Document;
import org.protobeans.exchange.ExchangeFactory;
import org.protobeans.exchange.exception.NotFoundException;
import org.protobeans.exchange.exception.RestResultException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;

public class Main {
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        var api = new ExchangeFactory().create("http://localhost:8787", ApiExchange.class, "mylonglongname@gmail.com", "123456");
        
        try {
            api.saveDocument(new Document("123"));
        } catch (RestResultException e) {
            // данное исключение должно быть проброшено выше для обработки в RestResultControllerAdvice
            // ExceptionControllerAdvice превращает это исключение в json-объект RestResult,
            // таким образом данное исключение будет корректно прокинуто через несколько микросервисов до конечного
            // UI-контроллера и далее пользователю            
            System.out.println(e.getRestResult()); // просто логируем
        }
        
        try {
            api.getDocument("123");
        } catch (NotFoundException e) {
            // обработка данного исключения имеет смысл в бизнес логике, также оно автоматически обрабатывается в RestResultControllerAdvice
            // ExceptionControllerAdvice превращает это исключение в json-объект RestResult,
            // таким образом данное исключение будет корректно прокинуто через несколько микросервисов до конечного
            // UI-контроллера и далее пользователю
            System.out.println(e.getMessage()); // просто логируем
        }
        
        try {
            api.superSecuredMethod();
        } catch (AccessDeniedException e) {
            // данное исключение должно быть проброшено выше для обработки в SecurityRestResultControllerAdvice
            // SecurityControllerAdvice превращает это исключение в json-объект RestResult,
            // таким образом данное исключение будет корректно прокинуто через несколько микросервисов до конечного
            // UI-контроллера и далее пользователю
            System.out.println("Авторизация не пройдена"); // просто логируем
        }
        
        api = new ExchangeFactory().create("http://localhost:8787", ApiExchange.class, "fake_name", "fake_password");
        
        try {
            api.saveDocument(new Document("123"));
        } catch (AuthenticationException e) {
            // данное исключение должно быть проброшено выше для обработки в SecurityRestResultControllerAdvice
            // SecurityControllerAdvice превращает это исключение в json-объект RestResult,
            // таким образом данное исключение будет корректно прокинуто через несколько микросервисов до конечного
            // UI-контроллера и далее пользователю
            System.out.println("Аутентификация не пройдена"); // просто логируем
        }
    }
}
