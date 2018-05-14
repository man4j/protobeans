package org.protobeans.flyway.config;

import javax.annotation.PostConstruct;

import org.flywaydb.core.Flyway;
import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.flyway.annotation.EnableFlyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
@InjectFrom(EnableFlyway.class)
public class FlywayConfig {
    private static final Logger logger = LoggerFactory.getLogger(FlywayConfig.class);
    
    private String dbProtocol;
    
    private String dbHost;
    
    private String dbPort;
    
    private String schema;
    
    private String user;
    
    private String password;
    
    private boolean waitDb;
    
    @PostConstruct
    public void migrate() throws InterruptedException {
        Flyway fw = new Flyway();
        
        if (dbProtocol.contains("postgresql")) {        
            fw.setDataSource(dbProtocol + dbHost + ":" + dbPort + "/" + schema, user, password);
        } else {
            fw.setDataSource(dbProtocol + dbHost + ":" + dbPort, user, password);
        }
        
        fw.setLocations("classpath:migrations");
        
        while (true) {
            try {
                fw.migrate();
                break;
            } catch (Exception e) {
                if (e.getMessage() != null && e.getMessage().contains("Unable to obtain Jdbc connection") && waitDb) {
                    logger.warn(e.getMessage(), e);
                    logger.warn("Waiting for database...");
                    
                    Thread.sleep(1000);
                    
                    continue;
                }
                
                throw e;
            }
        }
    }
}
