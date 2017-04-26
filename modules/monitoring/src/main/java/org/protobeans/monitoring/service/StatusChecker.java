package org.protobeans.monitoring.service;

import org.protobeans.monitoring.model.MonitoringProtocol;
import org.protobeans.monitoring.model.MonitoringType;

import com.spotify.docker.client.messages.swarm.Node;
import com.spotify.docker.client.messages.swarm.Task;

public interface StatusChecker {
    void checkStatus(String ip, int port, Task task, Node node) throws Exception;
    
    MonitoringType getMonitoringType();
    
    MonitoringProtocol getMonitoringProtocol();
}
