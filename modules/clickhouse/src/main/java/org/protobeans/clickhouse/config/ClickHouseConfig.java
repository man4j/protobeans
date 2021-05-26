package org.protobeans.clickhouse.config;

import javax.sql.DataSource;

import org.protobeans.clickhouse.annotation.EnableClickHouse;
import org.protobeans.core.annotation.InjectFrom;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import ru.yandex.clickhouse.ClickHouseDataSource;

@Configuration
@InjectFrom(EnableClickHouse.class)
@EnableTransactionManagement(proxyTargetClass = true)
public class ClickHouseConfig {
    private String dbHost;
    
    private String dbPort;
    
    private String schema;
    
    @Bean
    public ClickHouseDataSource dataSource() throws Exception {
        return new ClickHouseDataSource(String.format("jdbc:clickhouse://%s:%s/%s", dbHost, dbPort, schema));
    }
}
