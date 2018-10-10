package org.protobeans.mvc.rest.exception;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class BusinessException extends RuntimeException {
    private List<Entry<String, Object[]>> messages = new ArrayList<>();
    
    public BusinessException(String message) {
        messages.add(new SimpleEntry<>(message, null));
    }
    
    public BusinessException(String message, Object... params) {
        messages.add(new SimpleEntry<>(message, params));
    }
    
    public BusinessException(List<String> msg) {
        for (String m : msg) {
            messages.add(new SimpleEntry<>(m, null));
        }
    }

    public List<String> getMessages() {
        return messages.stream().map(Entry::getKey).collect(Collectors.toList());
    }
    
    public List<Entry<String, Object[]>> getMessagesWithParams() {
        return messages;
    }
    
    @Override
    public String getMessage() {
        return messages.stream().map(Entry::getKey).collect(Collectors.joining("\n"));
    }
}
