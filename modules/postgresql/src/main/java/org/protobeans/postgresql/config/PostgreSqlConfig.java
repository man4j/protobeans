package org.protobeans.postgresql.config;

import java.sql.Connection;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.postgresql.ds.PGSimpleDataSource;
import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.postgresql.annotation.EnablePostgreSql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@InjectFrom(EnablePostgreSql.class)
@EnableTransactionManagement(proxyTargetClass = true)
public class PostgreSqlConfig {
    private static Logger logger = LoggerFactory.getLogger(PostgreSqlConfig.class);
    
    private String dbHost;
    
    private String dbPort;
    
    private String schema;
    
    private String user;
    
    private String password;
    
    private int maxPoolSize;
    
    private String transactionIsolation;
    
    private boolean reindexOnStart;
    
    @Bean(destroyMethod = "close")
    public DataSource dataSource() throws Exception {
        HikariDataSource ds = new HikariDataSource();
        
        PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setUser(user);
        pgSimpleDataSource.setPassword(password);
        pgSimpleDataSource.setServerName(dbHost);
        pgSimpleDataSource.setPortNumber(Integer.parseInt(dbPort));
        pgSimpleDataSource.setDatabaseName(schema);
        pgSimpleDataSource.setLoadBalanceHosts(true);
        pgSimpleDataSource.setSsl(false);
        pgSimpleDataSource.setReWriteBatchedInserts(true);
        
        System.out.println("[PROTOBEANS]: Use postgres URL: " + pgSimpleDataSource.getUrl());
        
        ds.setDataSource(pgSimpleDataSource);
        ds.setMaximumPoolSize(maxPoolSize);
        ds.setAutoCommit(false);
        ds.setTransactionIsolation(transactionIsolation);
        ds.setMaxLifetime(TimeUnit.MINUTES.toMillis(10));

        if (reindexOnStart) {
            try(Connection con = ds.getConnection();            
                Statement st = con.createStatement()) {
                
                logger.info("[PROTOBEANS]: Start reindex database: " + schema);
                
                long t = System.currentTimeMillis();
                
                st.executeQuery("REINDEX DATABASE " + schema);
                
                logger.info("[PROTOBEANS]: Reindex database duration: " + (System.currentTimeMillis() - t) + " ms");
            }
        }
        
        return ds;
    }
}
