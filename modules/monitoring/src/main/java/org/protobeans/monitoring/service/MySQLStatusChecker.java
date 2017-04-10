package org.protobeans.monitoring.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.protobeans.monitoring.model.MonitoringProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.logstash.logback.marker.Markers;

abstract public class MySQLStatusChecker implements StatusChecker {
    private static final Logger logger = LoggerFactory.getLogger(MySQLStatusChecker.class);
    
    @Override
    public void checkStatus(String ip, int port) throws Exception {
        String rootPassword = System.getenv("MYSQL_ROOT_PASSWORD");
        
        String connectionUrl = "jdbc:mysql://" + ip + ":" + port + "/mysql?useSSL=false&user=root&password=" + rootPassword;
        
        logger.info(Markers.append("monitoringType", getMonitoringType()), "Get status from url: " + connectionUrl);
        
        try (Connection conn = DriverManager.getConnection(connectionUrl)) {
            checkStatus(conn);
        }
    }

    @Override
    public MonitoringProtocol getMonitoringProtocol() {
        return MonitoringProtocol.MYSQL;
    }

    public abstract void checkStatus(Connection conn) throws SQLException;
}
