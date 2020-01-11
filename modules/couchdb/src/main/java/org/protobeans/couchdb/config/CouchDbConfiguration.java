package org.protobeans.couchdb.config;

import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.couchdb.annotation.EnableCouchDb;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.equiron.acc.CouchDbConfig;

@Configuration
@InjectFrom(EnableCouchDb.class)
public class CouchDbConfiguration {
    private String dbHost;
    
    private String dbPort;
    
    private String user;
    
    private String password;
    
    @Bean
    public CouchDbConfig couchDbConfig() {
        CouchDbConfig.Builder builder = new CouchDbConfig.Builder().setIp(dbHost)
                                                                   .setPort(Integer.parseInt(dbPort));
        
        if (password != null) {
            builder.setUser(user).setPassword(password);
        }
        
        return builder.build();
    }
}
