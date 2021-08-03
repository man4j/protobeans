package org.protobeans.postgresql.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;
import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.hibernate.config.JacksonSupplier;
import org.protobeans.postgresql.annotation.EnablePostgreSql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    
    private boolean disablePreparedStatements;
    
    private String showSql;
    
    private String dialect;
    
    private String enableStatistics;
    
    private String[] basePackages;
    
    private int batchSize;
    
    private String migrationsPath;
    
    @Autowired(required = false)
    private ObjectMapper mapper;
    
    @Bean(destroyMethod = "close")
    public DataSource pgDataSource() throws Exception {
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
        
        if (disablePreparedStatements) {
            pgSimpleDataSource.setPrepareThreshold(0);
        }
        
        System.out.println("[PROTOBEANS]: Use postgres URL: " + pgSimpleDataSource.getUrl());
        
        ds.setDataSource(pgSimpleDataSource);

        if (maxPoolSize.equals("auto")) {
            ds.setMaximumPoolSize(Runtime.getRuntime().availableProcessors() * 4);
        } else {
            ds.setMaximumPoolSize(Integer.parseInt(maxPoolSize));
        }
        
        ds.setAutoCommit(false);
        ds.setTransactionIsolation(transactionIsolation);
        ds.setKeepaliveTime(60_000);

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
    
    @Bean
    public LocalContainerEntityManagerFactoryBean pgEntityManager() throws Exception {
       LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
       
       em.setDataSource(pgDataSource());
       HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
       jpaVendorAdapter.setShowSql("true".equals(showSql));
//       jpaVendorAdapter.setDatabase(dialect);
       
       JacksonSupplier.objectMapper = mapper;
       
       em.setJpaPropertyMap(new HashMap<String, Object>() {{put("hibernate.id.new_generator_mappings", true);
                                                            put("hibernate.format_sql", true);
                                                            put("hibernate.jdbc.batch_size", batchSize);
                                                            put("hibernate.order_inserts", true);
                                                            put("hibernate.order_updates", true);
                                                            put("hibernate.auto_quote_keyword", true);
                                                            put("hibernate.physical_naming_strategy", "org.protobeans.hibernate.config.ProtobeansNamingStrategy");
                                                            put("hibernate.dialect", dialect);
                                                            
                                                            //if connection pool already disables autocommit
                                                            put("hibernate.connection.provider_disables_autocommit", true);
                                                            
                                                            //this option prevent connecting to database before flyway
                                                            put("hibernate.temp.use_jdbc_metadata_defaults", false);
                                                            
                                                            if ("true".equals(enableStatistics)) {
                                                                put("hibernate.generate_statistics", true);
                                                            }
                                                          }});
       
       em.setJpaVendorAdapter(jpaVendorAdapter);
       
       em.setPackagesToScan(append(basePackages, "org.protobeans.hibernate.entity"));
       
       return em;
    }
    
    private <T> T[] append(T[] arr, T element) {
        final int N = arr.length;
        arr = Arrays.copyOf(arr, N + 1);
        arr[N] = element;
        return arr;
    }
    
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
        Flyway fw = Flyway.configure().ignoreMissingMigrations(true)
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
