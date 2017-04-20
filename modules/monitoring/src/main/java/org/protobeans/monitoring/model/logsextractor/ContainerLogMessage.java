package org.protobeans.monitoring.model.logsextractor;

import java.util.HashMap;
import java.util.Map;

import org.protobeans.monitoring.model.ContainerInfo;

public class ContainerLogMessage {
    public static enum LogChannel {STDOUT, STDERR}
    
    private ContainerInfo containerInfo;
    
    private String line;
    
    private LogChannel logChannel;
    
    private Map<String, Object> metadata = new HashMap<>();

    public ContainerLogMessage(ContainerInfo containerInfo, String line, LogChannel logChannel) {
        this.containerInfo = containerInfo;
        this.line = line;
        this.logChannel = logChannel;
    }
    
    public void addToLine(String addedLine) {
        this.line += addedLine;
    }

    public String getLine() {
        return line;
    }
    
    public void setLine(String line) {
        this.line = line;
    }

    public LogChannel getLogChannel() {
        return logChannel;
    }
    
    public ContainerInfo getContainerInfo() {
        return containerInfo;
    }
    
    public Map<String, Object> getMetadata() {
        return metadata;
    }
}
