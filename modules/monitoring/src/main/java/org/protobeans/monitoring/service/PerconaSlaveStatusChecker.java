package org.protobeans.monitoring.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.protobeans.monitoring.model.MonitoringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import net.logstash.logback.marker.Markers;

@Service
public class PerconaSlaveStatusChecker extends MySQLStatusChecker {
    private static final Logger logger = LoggerFactory.getLogger(PerconaSlaveStatusChecker.class);
    
    @Override
    public void checkStatus(Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("show slave status");
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                String masterHost = rs.getString("Master_Host");
                Long masterLag = Long.parseLong(rs.getString("Seconds_Behind_Master"));
                String serverId = MySQLStatusUtils.rs2Map(conn, "show variables like 'server_id'").values().iterator().next();
                
                logger.info(Markers.append("monitoringType", MonitoringType.PERCONA_SLAVE.name()).and(
                            Markers.append("percona_slave_master_host", masterHost)).and(
                            Markers.append("percona_slave_seconds_behind_master", masterLag)).and(
                            Markers.append("percona_slave_server_id", serverId)), "");
            }
        }
    }

    @Override
    public MonitoringType getMonitoringType() {
        return MonitoringType.PERCONA_SLAVE;
    }
}
