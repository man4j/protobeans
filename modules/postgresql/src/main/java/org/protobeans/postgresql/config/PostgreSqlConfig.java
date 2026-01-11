package org.protobeans.postgresql.config;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Properties;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.hibernate.cfg.BatchSettings;
import org.hibernate.cfg.JdbcSettings;
import org.hibernate.cfg.MappingSettings;
import org.hibernate.cfg.StatisticsSettings;
import org.postgresql.ds.PGSimpleDataSource;
import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.postgresql.annotation.EnablePostgreSql;
import org.protobeans.postgresql.mapper.ProtobeansJsonFormatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import com.zaxxer.hikari.HikariDataSource;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.json.JsonMapper;

@Configuration
@InjectFrom(EnablePostgreSql.class)
@EnableTransactionManagement(proxyTargetClass = true)
@Slf4j
public class PostgreSqlConfig {
    private String dbHost;
    
    private String dbPort;
    
    private String schema;
    
    private String user;
    
    private String password;
    
    private String maxPoolSize;
    
    private String transactionIsolation;
    
    private String showSql;
    
    private String enableStatistics;
    
    private String[] basePackages;
    
    private int batchSize;
    
    private String migrationsPath;
    
    @Autowired JsonMapper mapper;
    
    @Bean(destroyMethod = "close")
    public DataSource pgDataSource() throws Exception {
        var url = String.format("jdbc:postgresql://%s:%s/postgres", dbHost, dbPort);
        log.info("Check database exists: {}", url);
        
        var props = new Properties();
        props.put("user", user);
        props.put("password", password);
        props.put("ssl", false);
        
        try (var conn = new org.postgresql.Driver().connect(url, props);
             PreparedStatement ps = conn.prepareStatement("SELECT FROM pg_database WHERE datname = ?");) {
            ps.setString(1, schema);
            
            try (var rs = ps.executeQuery()) {
                if (!rs.next()) {
                    log.info("Create database: {}", schema);
                    
                    try (PreparedStatement ps1 = conn.prepareStatement(String.format("CREATE DATABASE %s", schema))) {
                        ps1.execute();
                    }
                    
                    try (PreparedStatement ps1 = conn.prepareStatement(String.format("GRANT ALL PRIVILEGES ON DATABASE %s TO %s", schema, user))) {
                        ps1.execute();
                    }
                } else {
                    log.info("Database {} already exists", schema);
                }
            }
        }
        
        var pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setUser(user);
        pgSimpleDataSource.setPassword(password);
        pgSimpleDataSource.setServerNames(new String[] {dbHost});
        pgSimpleDataSource.setPortNumbers(new int[] {Integer.parseInt(dbPort)});
        pgSimpleDataSource.setDatabaseName(schema);
        pgSimpleDataSource.setLoadBalanceHosts(true);
        pgSimpleDataSource.setSsl(false);
        pgSimpleDataSource.setReWriteBatchedInserts(true);
        
        log.info("[PROTOBEANS]: Use postgres URL: " + pgSimpleDataSource.getUrl());
        
        var ds = new HikariDataSource();
        ds.setDataSource(pgSimpleDataSource);
        ds.setMaximumPoolSize(maxPoolSize.equals("auto") ? Runtime.getRuntime().availableProcessors() * 4 : Integer.parseInt(maxPoolSize));
        ds.setAutoCommit(false);
        ds.setTransactionIsolation(transactionIsolation);
        ds.setKeepaliveTime(60_000);

        return ds;
    }
    
    @Bean
    public LocalContainerEntityManagerFactoryBean pgEntityManager() throws Exception {
        var jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setShowSql("true".equals(showSql));
        
       var em = new LocalContainerEntityManagerFactoryBean();
       
       em.setDataSource(pgDataSource());
       em.setJpaPropertyMap(new HashMap<String, Object>() {{
           put(JdbcSettings.FORMAT_SQL, true);
           
           // configuration property which tells Hibernate that the underlying JDBC Connections already disabled the auto-commit mode
           put(JdbcSettings.CONNECTION_PROVIDER_DISABLES_AUTOCOMMIT, true);
           
           put(BatchSettings.STATEMENT_BATCH_SIZE, batchSize);
           put(BatchSettings.ORDER_INSERTS, true);
           put(BatchSettings.ORDER_UPDATES, true);
           
           put(MappingSettings.GLOBALLY_QUOTED_IDENTIFIERS, true);
           put(MappingSettings.KEYWORD_AUTO_QUOTING_ENABLED, true);
           put(MappingSettings.PHYSICAL_NAMING_STRATEGY, ProtobeansNamingStrategy.class.getName());
           put(MappingSettings.JSON_FORMAT_MAPPER, new ProtobeansJsonFormatMapper(mapper));
           
           put(StatisticsSettings.GENERATE_STATISTICS, "true".equals(enableStatistics));
       }});
       
       em.setJpaVendorAdapter(jpaVendorAdapter);
       em.setPackagesToScan(basePackages);
       
       return em;
    }
    
    @SuppressWarnings("resource")
    @Bean
    public PlatformTransactionManager pgTransactionManager() throws Exception {
        return new JpaTransactionManager(pgEntityManager().getObject());
    }
    
    @Bean
    public TransactionTemplate pgTransactionTemplate() throws Exception {
        return new TransactionTemplate(pgTransactionManager());
    }
    
    @PostConstruct
    public void migrate() throws Exception {
        var fw = Flyway.configure().ignoreMigrationPatterns("*:missing")
                                   .validateOnMigrate(false)
                                   .locations("classpath:" + migrationsPath)
                                   .dataSource(pgDataSource())
                                   .baselineOnMigrate(true)
                                   .load();
        
        while (true) {
            try {
                fw.migrate();
                break;
            } catch (Exception e) {
                if (e.getMessage() != null && e.getMessage().contains("Unable to obtain Jdbc connection")) {
                    log.warn(e.getMessage(), e);
                    log.warn("Waiting for database...");
                    
                    Thread.sleep(1000);
                    
                    continue;
                }

                log.error("", e);
                System.exit(1);
            }
        }
    }
}
