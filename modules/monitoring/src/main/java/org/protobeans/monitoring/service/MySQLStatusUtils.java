package org.protobeans.monitoring.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MySQLStatusUtils {
    public static Map<String, String> rs2Map(Connection conn, String query) throws SQLException {
        Map<String, String> map = new HashMap<>();
        
        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                map.put(rs.getString("Variable_name"), rs.getString("Value"));
            }
        }
        
        return map;
    }
}
