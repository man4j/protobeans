package org.protobeans.hibernate.config;

import java.util.Arrays;
import java.util.HashMap;

import javax.sql.DataSource;

import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.hibernate.annotation.EnableHibernate;
import org.protobeans.hibernate.service.HibernateStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@InjectFrom(EnableHibernate.class)
public class HibernateConfig {
    private String showSql;
    
    private Database dialect;
    
    private String enableStatistics;
    
    private String[] basePackages;
    
    private int batchSize;
    
    @Autowired
    private DataSource dataSource;
    
    @Autowired(required = false)
    private ObjectMapper mapper;
    
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
       LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
       
       em.setDataSource(dataSource);
       HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
       jpaVendorAdapter.setShowSql("true".equals(showSql));
       jpaVendorAdapter.setDatabase(dialect);
       
       JacksonSupplier.objectMapper = mapper;
       
       em.setJpaPropertyMap(new HashMap<String, Object>() {{put("hibernate.id.new_generator_mappings", true);
                                                            put("hibernate.format_sql", true);
                                                            put("hibernate.jdbc.batch_size", batchSize);
                                                            put("hibernate.order_inserts", true);
                                                            put("hibernate.order_updates", true);
                                                            put("hibernate.auto_quote_keyword", true);
                                                            put("hibernate.physical_naming_strategy", "org.protobeans.hibernate.config.ProtobeansNamingStrategy");
                                                            
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
    public PlatformTransactionManager txManager() {
        return new JpaTransactionManager(entityManagerFactory().getObject());
    }
    
    @Bean
    public TransactionTemplate transactionTemplate() {
        return new TransactionTemplate(txManager());
    }
    
    @Bean
    public HibernateStatsService hibernateStatsService() {
        return new HibernateStatsService();
    }
}