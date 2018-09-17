package org.protobeans.flyway.config;

import javax.annotation.PostConstruct;

import org.flywaydb.core.Flyway;
import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.flyway.annotation.EnableFlyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@InjectFrom(EnableFlyway.class)
public class FlywayConfig {
    private static final Logger logger = LoggerFactory.getLogger(FlywayConfig.class);
    
    @Autowired
    private HikariDataSource dataSource;
    
    @PostConstruct
    public void migrate() throws InterruptedException {
        Flyway fw = new Flyway();
        
        fw.setDataSource(dataSource.getDataSource() == null ? dataSource : dataSource.getDataSource());
        fw.setLocations("classpath:migrations");
        
        while (true) {
            try {
                fw.repair();
                fw.migrate();
                break;
            } catch (Exception e) {
                if (e.getMessage() != null && e.getMessage().contains("Unable to obtain Jdbc connection")) {
                    logger.warn(e.getMessage(), e);
                    logger.warn("Waiting for database...");
                    
                    Thread.sleep(1000);
                    
                    continue;
                }

                logger.error("", e);
                
                System.exit(1);
            }
        }
    }
}
