package org.protobeans.monitoring.model.galera;

import java.util.Objects;

public class GaleraStatusKey {
    private String clusterName;
    
    private String nodeAddress;

    public GaleraStatusKey(String clusterName, String nodeAddress) {
        this.clusterName = clusterName;
        this.nodeAddress = nodeAddress;
    }
    
    public String getClusterName() {
        return clusterName;
    }

    public String getNodeAddress() {
        return nodeAddress;
    }

    @Override
    public int hashCode() {
        return Objects.hash(clusterName, nodeAddress);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;

        if (this == obj) return true;
        
        if (this.getClass() != obj.getClass()) return false;
        
        GaleraStatusKey other = (GaleraStatusKey)obj;

        return Objects.equals(clusterName, other.clusterName) && Objects.equals(nodeAddress, other.nodeAddress);
    }
}
