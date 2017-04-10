package org.protobeans.monitoring.service;

import org.protobeans.monitoring.model.MonitoringProtocol;
import org.protobeans.monitoring.model.MonitoringType;

public interface StatusChecker {
    void checkStatus(String ip, int port) throws Exception;
    
    MonitoringType getMonitoringType();
    
    MonitoringProtocol getMonitoringProtocol();
}
