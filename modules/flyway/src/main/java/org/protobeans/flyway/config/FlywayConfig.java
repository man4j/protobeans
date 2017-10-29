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
    
    private String dbUrl;
    
    private String schema;
    
    private String user;
    
    private String password;
    
    private boolean waitDb;
    
    @PostConstruct
    public void migrate() throws InterruptedException {
        Flyway fw = new Flyway();
        
        fw.setSchemas(schema);
        fw.setDataSource(dbUrl, user, password);
        fw.setLocations("classpath:migrations");
        
        while (true) {
            try {
                fw.migrate();
                break;
            } catch (Exception e) {
                if (e.getMessage().contains("Unable to obtain Jdbc connection") && waitDb) {
                    logger.warn(e.getMessage());
                    logger.warn("Waiting for database...");
                    
                    Thread.sleep(1000);
                    
                    continue;
                }
                
                throw e;
            }
        }
    }
}
