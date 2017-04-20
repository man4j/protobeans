package org.protobeans.monitoring.service.logsextractor;

import org.protobeans.monitoring.model.logsextractor.ContainerLogMessage;

public abstract class LogProcessingStage {
    private LogProcessingStage next;
    
    public void setNext(LogProcessingStage next) {
        this.next = next;
    }
    
    public abstract void processLogMessage(ContainerLogMessage msg);
    
    public void processNext(ContainerLogMessage msg) {
        if (next != null) {
            next.processLogMessage(msg);
        }
    }
}
