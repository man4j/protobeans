package org.protobeans.couchdb.config;

import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.couchdb.annotation.EnableCouchDb;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.equiron.acc.CouchDbConfig;
import com.equiron.acc.provider.HttpClientProviderType;

@Configuration
@InjectFrom(EnableCouchDb.class)
public class CouchDbConfiguration {
    private String dbHost;
    
    private String dbPort;
    
    private String user;
    
    private String password;
    
    private HttpClientProviderType httpClientProviderType;
    
    @Bean
    public CouchDbConfig couchDbConfig() {
        CouchDbConfig.Builder builder = new CouchDbConfig.Builder().setHost(dbHost)
                                                                   .setPort(Integer.parseInt(dbPort))
                                                                   .setHttpClientProviderType(httpClientProviderType);
        
        if (password != null) {
            builder.setUser(user).setPassword(password);
        }
        
        return builder.build();
    }
}
