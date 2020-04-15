package org.protobeans.postgresql.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Properties;
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
    
    private String maxPoolSize;
    
    private String transactionIsolation;
    
    private boolean reindexOnStart;
    
    @Bean(destroyMethod = "close")
    public DataSource dataSource() throws Exception {
        String url = String.format("jdbc:postgresql://%s:%s/postgres", dbHost, dbPort);
        
        logger.info("Check database exists: {}", url);
        
        org.postgresql.Driver driver = new org.postgresql.Driver();

        Properties props = new Properties();
        props.put("user", user);
        props.put("password", password);
        props.put("ssl", false);
        
        try (Connection conn = driver.connect(url, props);
             PreparedStatement ps = conn.prepareStatement("SELECT FROM pg_database WHERE datname = ?");) {
            ps.setString(1, schema);
            
            if (!ps.executeQuery().next()) {
                logger.info("Create database: {}", schema);
                
                try (PreparedStatement ps1 = conn.prepareStatement(String.format("CREATE DATABASE %s", schema))) {
                    ps1.execute();
                }
                
                try (PreparedStatement ps1 = conn.prepareStatement(String.format("GRANT ALL PRIVILEGES ON DATABASE %s TO %s", schema, user))) {
                    ps1.execute();
                }
            } else {
                logger.info("Database {} already exists", schema);
            }
        }
        
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
        ds.setMaximumPoolSize(Integer.parseInt(maxPoolSize));
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
