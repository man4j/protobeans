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
    private String[] suffixes;
    
    public MultilineProcessingStage(String[] prefixes, String[] suffixes) {
        this.prefixes = prefixes;
        this.suffixes = suffixes;
    }

    @Override
    public void processLogMessage(ContainerLogMessage messagePart) {
        boolean newLine = !Arrays.stream(prefixes).anyMatch(messagePart.getLine()::startsWith);
        boolean beginLine = Arrays.stream(suffixes).anyMatch(messagePart.getLine().replaceAll("\\R", "").trim()::endsWith);
        
        Map<String, ContainerLogMessage> map = messagePart.getLogChannel() == LogChannel.STDOUT ? container2StdMessage : container2ErrMessage;
        
        String containerId = messagePart.getContainerInfo().getContainer().id();
        
        ContainerLogMessage accumulatedMessage = map.get(containerId);
        
        if (accumulatedMessage != null) {
            if (newLine) {
                processNext(accumulatedMessage);
                
                if (!beginLine) {
                    processNext(messagePart);
                    map.remove(containerId);                    
                } else {
                    map.put(containerId, messagePart);
                }
            } else {
                accumulatedMessage.addToLine("\n" + messagePart.getLine());
            }
        } else {
            if (newLine && !beginLine) {
                processNext(messagePart);
            } else {
                map.put(containerId, messagePart);
            }
        }
    }
}
