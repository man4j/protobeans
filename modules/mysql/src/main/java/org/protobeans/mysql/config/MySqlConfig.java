package org.protobeans.mysql.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.mysql.annotation.EnableMySql;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@InjectFrom(EnableMySql.class)
@EnableTransactionManagement(proxyTargetClass = true)
public class MySqlConfig {
    private String dbUrl;
    
    private String user;
    
    private String password;
    
    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        HikariDataSource ds = new HikariDataSource();
        
        ds.setJdbcUrl(dbUrl);
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUsername(user);
        ds.setMaximumPoolSize(50);
        ds.setPassword(password);

        ds.addDataSourceProperty("useSSL", "false");
        ds.addDataSourceProperty("characterEncoding", "UTF-8");
        ds.addDataSourceProperty("cachePrepStmts", "true");
        ds.addDataSourceProperty("prepStmtCacheSize", "250");
        ds.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        
        return ds;
    }
}
