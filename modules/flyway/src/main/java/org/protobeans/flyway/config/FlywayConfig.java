package org.protobeans.flyway.config;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.flyway.annotation.EnableFlyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@InjectFrom(EnableFlyway.class)
public class FlywayConfig {
    private static final Logger logger = LoggerFactory.getLogger(FlywayConfig.class);

    private String url;
    
    private String user;
    
    private String password;
    
    private boolean repair;
    
    @Autowired
    private ApplicationContext ctx;
        
    @PostConstruct
    public void migrate() throws InterruptedException {
        FluentConfiguration fwConfig = Flyway.configure().ignoreMissingMigrations(true)
                                                         .locations("classpath:migrations");
        
        if (url.isBlank()) {
            HikariDataSource hikariDs = null;
            
            try {
                hikariDs = ctx.getBean(HikariDataSource.class);
            } catch (NoSuchBeanDefinitionException e) {
                //empty
            }
            
            if (hikariDs != null) {            
                fwConfig.dataSource(hikariDs.getDataSource() == null ? hikariDs : hikariDs.getDataSource());
            } else {
                fwConfig.dataSource(ctx.getBean(DataSource.class));
            }
        } else {
            fwConfig.dataSource(url, user, password);
        }
        
        Flyway fw = fwConfig.load();
        
        while (true) {
            try {
                if (repair) {
                    fw.repair();
                }
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
