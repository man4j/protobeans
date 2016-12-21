package org.protobeans.flyway.config;

import javax.annotation.PostConstruct;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {
    private String dbUrl;
    
    private String user;
    
    private String password;
    
    @PostConstruct
    public void migrate() {
        Flyway fw = new Flyway();
        fw.setDataSource(dbUrl, user, password);
        fw.setLocations("classpath:migrations");
        fw.migrate();
    }
}
