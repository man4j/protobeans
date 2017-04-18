package org.protobeans.monitoring.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.protobeans.monitoring.config.ServiceMonitoringConfig;
import org.protobeans.monitoring.model.MonitoringType;
import org.protobeans.monitoring.model.galera.GaleraStatus;
import org.protobeans.monitoring.model.galera.GaleraStatusKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import net.logstash.logback.marker.Markers;

@Service
public class GaleraStatusChecker extends MySQLStatusChecker {
    private static final Logger logger = LoggerFactory.getLogger(GaleraStatusChecker.class);
    
    private Map<GaleraStatusKey, GaleraStatus> statusMap;
    
    public GaleraStatusChecker() {
        Cache<GaleraStatusKey, GaleraStatus> cache = CacheBuilder.newBuilder().expireAfterWrite(ServiceMonitoringConfig.CHECK_INTERVAL * 2, TimeUnit.SECONDS).<GaleraStatusKey, GaleraStatus>build();
        
        statusMap = cache.asMap();
    }

    @Override
    public void checkStatus(Connection conn) throws SQLException {
        GaleraStatus currentStatus = fetchStatus(conn);
        
        if (currentStatus != null) {
            GaleraStatusKey key = currentStatus.getGaleraStatusKey();
            
            if (statusMap.containsKey(key)) {//если есть предыдущее значение         
                GaleraStatus prevStatus = statusMap.get(key);
                
                write(currentStatus, prevStatus);
            }
            
            statusMap.put(key, currentStatus);
        }
    }
    
    private GaleraStatus fetchStatus(Connection conn) throws SQLException {
        Map<String, String> wsrepStatus = MySQLStatusUtils.rs2Map(conn, "show status like 'wsrep_%'");
        Map<String, String> wsrepVars = MySQLStatusUtils.rs2Map(conn, "show variables like 'wsrep_%'");
        
        if (wsrepVars.get("wsrep_cluster_name") == null) return null;
        
        GaleraStatus status = new GaleraStatus(new GaleraStatusKey(wsrepVars.get("wsrep_cluster_name"), wsrepVars.get("wsrep_node_address")));
        
        status.setWsrep_flow_control_paused_ns(Long.parseLong(wsrepStatus.get("wsrep_flow_control_paused_ns")));
        status.setWsrep_flow_control_recv(Long.parseLong(wsrepStatus.get("wsrep_flow_control_recv")));
        status.setWsrep_flow_control_sent(Long.parseLong(wsrepStatus.get("wsrep_flow_control_sent")));
        status.setWsrep_local_commits(Long.parseLong(wsrepStatus.get("wsrep_local_commits")));
        status.setWsrep_received_bytes(Long.parseLong(wsrepStatus.get("wsrep_received_bytes")));
        status.setWsrep_replicated_bytes(Long.parseLong(wsrepStatus.get("wsrep_replicated_bytes")));
        status.setClusterSize(Long.parseLong(wsrepStatus.get("wsrep_cluster_size")));
        status.setWsrep_local_state_comment(wsrepStatus.get("wsrep_local_state_comment"));
        status.setWsrep_local_state(Long.parseLong(wsrepStatus.get("wsrep_local_state")));
        status.setWsrep_local_recv_queue(Long.parseLong(wsrepStatus.get("wsrep_local_recv_queue")));
        status.setWsrep_local_send_queue(Long.parseLong(wsrepStatus.get("wsrep_local_send_queue")));
        status.setWsrep_local_bf_aborts(Long.parseLong(wsrepStatus.get("wsrep_local_bf_aborts")));
        status.setWsrep_local_cert_failures(Long.parseLong(wsrepStatus.get("wsrep_local_cert_failures")));
        status.setWsrep_cluster_status(wsrepStatus.get("wsrep_cluster_status"));
        
        return status;
    }
    
    private void write(GaleraStatus status, GaleraStatus prevStatus) {
        logger.info(Markers.append("monitoringType", MonitoringType.GALERA.name()).and(
                    Markers.append("wsrep_cluster_name", status.getGaleraStatusKey().getClusterName())).and(
                    Markers.append("wsrep_node_address", status.getGaleraStatusKey().getNodeAddress())).and(
                    Markers.append("wsrep_cluster_size", status.getClusterSize())).and(
                    Markers.append("wsrep_local_commits", status.getWsrep_local_commits() - prevStatus.getWsrep_local_commits())).and(
                    Markers.append("wsrep_local_state_comment", status.getWsrep_local_state_comment())).and(
                    Markers.append("wsrep_local_state", status.getWsrep_local_state())).and(
                    Markers.append("wsrep_cluster_status", status.getWsrep_cluster_status())).and(
                    Markers.append("wsrep_replicated_bytes", status.getWsrep_replicated_bytes() - prevStatus.getWsrep_replicated_bytes())).and(
                    Markers.append("wsrep_received_bytes", status.getWsrep_received_bytes() - prevStatus.getWsrep_received_bytes())).and(
                    Markers.append("wsrep_local_send_queue", status.getWsrep_local_send_queue())).and(
                    Markers.append("wsrep_local_recv_queue", status.getWsrep_local_recv_queue())).and(
                    Markers.append("wsrep_flow_control_sent", status.getWsrep_flow_control_sent() - prevStatus.getWsrep_flow_control_sent())).and(
                    Markers.append("wsrep_flow_control_recv", status.getWsrep_flow_control_recv() - prevStatus.getWsrep_flow_control_recv())).and(
                    Markers.append("wsrep_local_bf_aborts", status.getWsrep_local_bf_aborts() - prevStatus.getWsrep_local_bf_aborts())).and(
                    Markers.append("wsrep_local_cert_failures", status.getWsrep_local_cert_failures() - prevStatus.getWsrep_local_cert_failures())).and(
                    Markers.append("wsrep_flow_control_paused_ns", status.getWsrep_flow_control_paused_ns() - prevStatus.getWsrep_flow_control_paused_ns())), "");
    }

    @Override
    public MonitoringType getMonitoringType() {
        return MonitoringType.GALERA;
    }
}
