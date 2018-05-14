package org.protobeans.social.service;

import java.sql.ResultSet;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.stereotype.Service;

@Service
public class SocialService {
    @Autowired
    private DataSource dataSource;

    public void updateSocialConnection(String oldUserId, String newUserId) {
        new JdbcTemplate(dataSource).update("UPDATE UserConnection SET userId = ? WHERE userId = ?", newUserId, oldUserId);
    }
    
    public ConnectionKey getConnectionKeyByUserId(String userId) {
        return new JdbcTemplate(dataSource).query("SELECT providerId, providerUserId FROM UserConnection WHERE userId = ?", 
                                                  new Object[] {userId}, 
                                                  (ResultSet rs) -> {rs.next(); return new ConnectionKey(rs.getString("providerId"), rs.getString("providerUserId"));});
    }
}
