package org.protobeans.monitoring.service.logsextractor;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.protobeans.monitoring.model.logsextractor.ContainerLogMessage;
import org.protobeans.monitoring.model.logsextractor.ContainerLogMessage.LogChannel;

public class MultilineProcessingStage extends LogProcessingStage {
    private ConcurrentHashMap<String, ContainerLogMessage> container2StdMessage = new ConcurrentHashMap<>();
    
    private ConcurrentHashMap<String, ContainerLogMessage> container2ErrMessage = new ConcurrentHashMap<>();
    
    private String[] prefixes;
    
    public MultilineProcessingStage(String... prefixes) {
        this.prefixes = prefixes;
    }

    @Override
    public void processLogMessage(ContainerLogMessage messagePart) {
        boolean newLine = !Arrays.stream(prefixes).anyMatch(messagePart.getLine()::startsWith);
        
        Map<String, ContainerLogMessage> map = messagePart.getLogChannel() == LogChannel.STDOUT ? container2StdMessage : container2ErrMessage;
        
        String containerId = messagePart.getContainerInfo().getContainer().id();
        
        ContainerLogMessage accumulatedMessage = map.get(containerId);
        
        if (accumulatedMessage != null) {
            if (newLine) {
                processNext(accumulatedMessage);
                
                map.put(containerId, messagePart);
            } else {
                accumulatedMessage.addToLine(messagePart.getLine());
            }
        } else {
            map.put(containerId, messagePart);
        }
    }
}
