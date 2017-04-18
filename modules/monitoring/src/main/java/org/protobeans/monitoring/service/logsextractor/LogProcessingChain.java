package org.protobeans.monitoring.service.logsextractor;

public class LogProcessingChain {
    private LogProcessingStage first;
    
    private LogProcessingStage last;

    public LogProcessingChain add(LogProcessingStage next) {
        if (first == null) {
            first = next;
            last = next;
        } else {
            last.setNext(next);
        }
        
        return this;
    }
    
    public LogProcessingStage getFirst() {
        return first;
    }
}
