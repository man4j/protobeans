package org.protobeans.postgresql.config;

import javax.sql.DataSource;

import org.postgresql.ds.PGSimpleDataSource;
import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.postgresql.annotation.EnablePostgreSql;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@InjectFrom(EnablePostgreSql.class)
@EnableTransactionManagement(proxyTargetClass = true)
public class PostgreSqlConfig {
    private String dbHost;
    
    private String dbPort;
    
    private String schema;
    
    private String user;
    
    private String password;
    
    private int maxPoolSize;
    
    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        HikariDataSource ds = new HikariDataSource();
        
        PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setUser(user);
        pgSimpleDataSource.setPassword(password);
        pgSimpleDataSource.setServerName(dbHost);
        pgSimpleDataSource.setPortNumber(Integer.parseInt(dbPort));
        pgSimpleDataSource.setDatabaseName(schema);
        pgSimpleDataSource.setLoadBalanceHosts(true);
        pgSimpleDataSource.setSsl(false);
        
        ds.setDataSource(pgSimpleDataSource);
        ds.setMaximumPoolSize(maxPoolSize);
        ds.setAutoCommit(false);
        ds.setTransactionIsolation("TRANSACTION_SERIALIZABLE");

        return ds;
    }
}
