package org.protobeans.mysql.config;

import static java.util.concurrent.TimeUnit.MINUTES;

import javax.sql.DataSource;

import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.mysql.annotation.EnableMySql;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@InjectFrom(EnableMySql.class)
@EnableTransactionManagement(proxyTargetClass = true)
public class MySqlConfig {
    private String dbHost;
    
    private String dbPort;
    
    private String schema;
    
    private String user;
    
    private String password;
    
    private int maxPoolSize;
    
    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        HikariDataSource ds = new HikariDataSource();
        
        ds.setJdbcUrl("jdbc:mysql://" + dbHost + ":" + dbPort + "/" + schema);
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUsername(user);
        ds.setMaximumPoolSize(maxPoolSize);
        ds.setPassword(password);
        ds.setAutoCommit(false);
        ds.setIdleTimeout(MINUTES.toMillis(2));
        ds.setMaxLifetime(MINUTES.toMillis(15));

        ds.addDataSourceProperty("useSSL", "false");
        ds.addDataSourceProperty("characterEncoding", "UTF-8");
        ds.addDataSourceProperty("cachePrepStmts", "true");
        ds.addDataSourceProperty("useServerPrepStmts ", "true");
        ds.addDataSourceProperty("prepStmtCacheSize", "250");
        ds.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        
        return ds;
    }
}
