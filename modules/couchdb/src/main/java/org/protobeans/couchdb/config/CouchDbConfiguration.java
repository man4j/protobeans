package org.protobeans.couchdb.config;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
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
    
    @SuppressWarnings("resource")
    @Bean
    public CouchDbConfig couchDbConfig() {
        AsyncHttpClient httpClient = new DefaultAsyncHttpClient(new DefaultAsyncHttpClientConfig.Builder().setRequestTimeout(-1).setReadTimeout(60_000).build());
        
        CouchDbConfig.Builder builder = new CouchDbConfig.Builder().setIp(dbHost)
                                                                   .setPort(Integer.parseInt(dbPort))
                                                                   .setHttpClient(httpClient);
        
        if (password != null) {
            builder.setUser(user).setPassword(password);
        }
        
        return builder.build();
    }
}
