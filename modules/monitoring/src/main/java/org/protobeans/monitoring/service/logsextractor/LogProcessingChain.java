package org.protobeans.monitoring.service.logsextractor;

public class LogProcessingChain {
    private LogProcessingStage first;
    
    private LogProcessingStage last;

    public LogProcessingChain add(LogProcessingStage next) {
        if (first == null) {
            first = next;
        } else {
            last.setNext(next);
        }
        
        last = next;
        
        return this;
    }
    
    public LogProcessingStage getFirst() {
        return first;
    }
}
