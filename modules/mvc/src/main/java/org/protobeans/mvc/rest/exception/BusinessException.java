package org.protobeans.mvc.rest.exception;

import java.util.ArrayList;
import java.util.List;

public class BusinessException extends RuntimeException {
    private List<String> messages = new ArrayList<>();
    
    public BusinessException(String message) {
        messages.add(message);
    }
    
    public BusinessException(List<String> messages) {
        this.messages = messages;
    }

    public List<String> getMessages() {
        return messages;
    }
    
    @Override
    public String getMessage() {
        String message = "";
        
        for (String msg : messages) {
            message += (msg + "\n");
        }
        
        return message;
    }
}
