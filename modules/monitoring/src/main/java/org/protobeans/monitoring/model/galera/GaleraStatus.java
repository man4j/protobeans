package org.protobeans.monitoring.model.galera;

public class GaleraStatus {
    private GaleraStatusKey galeraStatusKey;
    
    private long clusterSize;
    
    private String wsrep_local_state_comment;
    
    private long wsrep_local_state;
    
    private long wsrep_local_commits;
    
    private long wsrep_local_send_queue;
    
    private long wsrep_local_recv_queue;
    
    private long wsrep_replicated_bytes;
    
    private long wsrep_received_bytes;
    
    private long wsrep_flow_control_sent;
    
    private long wsrep_flow_control_recv;
    
    private long wsrep_flow_control_paused_ns;
    
    public GaleraStatus(GaleraStatusKey galeraStatusKey) {
        this.galeraStatusKey = galeraStatusKey;
    }

    public GaleraStatusKey getGaleraStatusKey() {
        return galeraStatusKey;
    }

    public long getClusterSize() {
        return clusterSize;
    }

    public void setClusterSize(long clusterSize) {
        this.clusterSize = clusterSize;
    }

    public String getWsrep_local_state_comment() {
        return wsrep_local_state_comment;
    }

    public void setWsrep_local_state_comment(String wsrep_local_state_comment) {
        this.wsrep_local_state_comment = wsrep_local_state_comment;
    }

    public long getWsrep_local_state() {
        return wsrep_local_state;
    }

    public void setWsrep_local_state(long wsrep_local_state) {
        this.wsrep_local_state = wsrep_local_state;
    }

    public long getWsrep_local_commits() {
        return wsrep_local_commits;
    }

    public void setWsrep_local_commits(long wsrep_local_commits) {
        this.wsrep_local_commits = wsrep_local_commits;
    }
    
    public long getWsrep_local_send_queue() {
        return wsrep_local_send_queue;
    }

    public void setWsrep_local_send_queue(long wsrep_local_send_queue) {
        this.wsrep_local_send_queue = wsrep_local_send_queue;
    }

    public long getWsrep_local_recv_queue() {
        return wsrep_local_recv_queue;
    }

    public void setWsrep_local_recv_queue(long wsrep_local_recv_queue) {
        this.wsrep_local_recv_queue = wsrep_local_recv_queue;
    }

    public long getWsrep_replicated_bytes() {
        return wsrep_replicated_bytes;
    }

    public void setWsrep_replicated_bytes(long wsrep_replicated_bytes) {
        this.wsrep_replicated_bytes = wsrep_replicated_bytes;
    }

    public long getWsrep_received_bytes() {
        return wsrep_received_bytes;
    }

    public void setWsrep_received_bytes(long wsrep_received_bytes) {
        this.wsrep_received_bytes = wsrep_received_bytes;
    }

    public long getWsrep_flow_control_sent() {
        return wsrep_flow_control_sent;
    }

    public void setWsrep_flow_control_sent(long wsrep_flow_control_sent) {
        this.wsrep_flow_control_sent = wsrep_flow_control_sent;
    }

    public long getWsrep_flow_control_recv() {
        return wsrep_flow_control_recv;
    }

    public void setWsrep_flow_control_recv(long wsrep_flow_control_recv) {
        this.wsrep_flow_control_recv = wsrep_flow_control_recv;
    }

    public long getWsrep_flow_control_paused_ns() {
        return wsrep_flow_control_paused_ns;
    }

    public void setWsrep_flow_control_paused_ns(long wsrep_flow_control_paused_ns) {
        this.wsrep_flow_control_paused_ns = wsrep_flow_control_paused_ns;
    }
}
