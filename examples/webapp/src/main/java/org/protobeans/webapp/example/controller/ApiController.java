package org.protobeans.webapp.example.controller;

import org.protobeans.client.ApiExchange;
import org.protobeans.client.model.Document;
import org.protobeans.exchange.exception.NotFoundException;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ApiController implements ApiExchange {    
    @Override
    public void saveDocument(Document document) {
        if (!document.getContent().equals("1")) {
            throw new RuntimeException("Ошибка при сохранении документа");
        }
    }

    @Override
    public Document getDocument(String docId) {
        if (!docId.equals("1")) {
            throw new NotFoundException("Документ не найден");
        }
        
        return new Document("1");
    }

    @Override
    public void superSecuredMethod() {
        // empty
    }
}
