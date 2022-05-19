package org.protobeans.cockroachdb.config;

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.hibernate.dialect.CockroachDB201Dialect;
import org.postgresql.ds.PGSimpleDataSource;
import org.protobeans.cockroachdb.annotation.EnableCockroachDb;
import org.protobeans.cockroachdb.transaction.RetryableTransactionTemplate;
import org.protobeans.core.annotation.InjectFrom;
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
@InjectFrom(EnableCockroachDb.class)
@EnableTransactionManagement(proxyTargetClass = true)
public class CockroachDbConfig {
    private static Logger logger = LoggerFactory.getLogger(CockroachDbConfig.class);

    private String dbHosts;

    private String dbPorts;

    private String schema;

    private String maxPoolSize;

    private String showSql;

    private String enableStatistics;

    private String[] basePackages;

    private int batchSize;

    private int fetchSize;

    private String migrationsPath;

    @Autowired(required = false)
    private ObjectMapper mapper;

    @Bean(destroyMethod = "close")
    public DataSource crdbDataSource() throws Exception {
        HikariDataSource ds = new HikariDataSource();

        PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setUser("root");
        pgSimpleDataSource.setServerNames(dbHosts.split(","));

        String[] _dbPorts = dbPorts.split(",");
        
        int[] ports = new int[_dbPorts.length];
        
        for (int i = 0; i < _dbPorts.length; i++) {
            ports[i] = Integer.parseInt(_dbPorts[i]);
        }
        
        pgSimpleDataSource.setPortNumbers(ports);

        pgSimpleDataSource.setDatabaseName(schema);
        pgSimpleDataSource.setLoadBalanceHosts(true);
        pgSimpleDataSource.setSsl(false);
        pgSimpleDataSource.setReWriteBatchedInserts(true);

        System.out.println("[PROTOBEANS]: Use cockroachDB URL: " + pgSimpleDataSource.getUrl());

        ds.setDataSource(pgSimpleDataSource);

        if (maxPoolSize.equals("auto")) {
            ds.setMaximumPoolSize(Runtime.getRuntime().availableProcessors() * 4);
        } else {
            ds.setMaximumPoolSize(Integer.parseInt(maxPoolSize));
        }

        ds.setAutoCommit(false);
        ds.setKeepaliveTime(60_000);

        return ds;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean crdbEntityManager() throws Exception {
       LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
       
       em.setDataSource(crdbDataSource());
       HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
       jpaVendorAdapter.setShowSql("true".equals(showSql));
       
       JacksonSupplier.objectMapper = mapper;
       
       em.setJpaPropertyMap(new HashMap<String, Object>() {{put("hibernate.id.new_generator_mappings", true);
                                                            put("hibernate.format_sql", true);
                                                            put("hibernate.jdbc.batch_size", batchSize);
                                                            put("hibernate.jdbc.fetch_size", fetchSize);//используется для курсоров насколько я понял
                                                            put("hibernate.order_inserts", true);
                                                            put("hibernate.order_updates", true);
                                                            put("hibernate.auto_quote_keyword", true);
                                                            put("hibernate.physical_naming_strategy", ProtobeansNamingStrategy.class.getName());
                                                            put("hibernate.dialect", CockroachDB201Dialect.class.getName());
                                                            put("hibernate.types.jackson.object.mapper", JacksonSupplier.class.getName());
                                                    
                                                            //if connection pool already disables autocommit
                                                            put("hibernate.connection.provider_disables_autocommit", true);
                                                            
                                                            //this option prevent connecting to database before flyway
                                                            put("hibernate.temp.use_jdbc_metadata_defaults", false);
                                                            
                                                            if ("true".equals(enableStatistics)) {
                                                                put("hibernate.generate_statistics", true);
                                                            }
                                                          }});
       
       em.setJpaVendorAdapter(jpaVendorAdapter);
       
       em.setPackagesToScan(basePackages);
       
       return em;
    }

    @Bean
    public PlatformTransactionManager crdbTransactionManager() throws Exception {
        return new JpaTransactionManager(crdbEntityManager().getObject());
    }

    @Bean
    public RetryableTransactionTemplate crdbTransactionTemplate() throws Exception {
        return new RetryableTransactionTemplate(new TransactionTemplate(crdbTransactionManager()));
    }

    @PostConstruct
    public void migrate() throws Exception {
        Flyway fw = Flyway.configure().ignoreMigrationPatterns("*:missing")
                                      .validateOnMigrate(false)
                                      .locations("classpath:" + migrationsPath)
                                      .dataSource(crdbDataSource())
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
