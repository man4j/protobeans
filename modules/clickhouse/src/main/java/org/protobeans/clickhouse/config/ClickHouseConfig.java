package org.protobeans.clickhouse.config;

import javax.annotation.PostConstruct;

import org.flywaydb.core.Flyway;
import org.protobeans.clickhouse.annotation.EnableClickHouse;
import org.protobeans.core.annotation.InjectFrom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import ru.yandex.clickhouse.ClickHouseDataSource;

@Configuration
@InjectFrom(EnableClickHouse.class)
@EnableTransactionManagement(proxyTargetClass = true)
public class ClickHouseConfig {
    private static Logger logger = LoggerFactory.getLogger(ClickHouseConfig.class);
    
    private String dbHost;
    
    private String dbPort;
    
    private String schema;
    
    private String migrationsPath;
    
    @Bean
    public ClickHouseDataSource clickHouseDataSource() throws Exception {
        return new ClickHouseDataSource(String.format("jdbc:clickhouse://%s:%s/%s", dbHost, dbPort, schema));
    }
    
    @Bean
    public JdbcTemplate clickhouseJdbcTemplate() throws Exception {
        return new JdbcTemplate(clickHouseDataSource());
    }
    
    @Bean
    public NamedParameterJdbcTemplate clickHouseNamedParameterJdbcTemplate() throws Exception {
        return new NamedParameterJdbcTemplate(clickhouseJdbcTemplate());
    }
    
    @PostConstruct
    public void migrate() throws Exception {
        Flyway fw = Flyway.configure().ignoreMissingMigrations(true)
                                      .validateOnMigrate(true)
                                      .locations("classpath:" + migrationsPath)
                                      .dataSource(clickHouseDataSource())
                                      .baselineOnMigrate(true)
                                      .load();
        while (true) {
            try {
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
