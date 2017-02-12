package org.protobeans.hibernate.config;

import java.util.HashMap;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.hibernate.dialect.Dialect;
import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.hibernate.annotation.EnableHibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
@InjectFrom(EnableHibernate.class)
public class HibernateConfig {
    private Class<? extends Dialect> dialect;
    
    private String showSql;
    
    private String enableStatistics;
    
    private Class<?>[] basePackageClasses;
    
    @Autowired
    private DataSource dataSource;
    
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
       LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
       
       em.setDataSource(dataSource);
       HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
       jpaVendorAdapter.setShowSql("true".equals(showSql));
       
       em.setJpaPropertyMap(new HashMap<String, Object>() {{put("hibernate.id.new_generator_mappings", true);
                                                            put("hibernate.dialect", dialect.getName());
                                                            put("hibernate.format_sql", true);
                                                            put("hibernate.jdbc.fetch_size", 100);
                                                            put("hibernate.jdbc.batch_size", 1000);
                                                            put("hibernate.order_inserts", true);
                                                            put("hibernate.order_updates", true);
                                                            put("hibernate.auto_quote_keyword", true);
                                                            
                                                            if ("true".equals(enableStatistics)) {
                                                                put("hibernate.generate_statistics", true);
                                                            }
                                                          }});
       em.setJpaVendorAdapter(jpaVendorAdapter);
       em.setPackagesToScan(Stream.of(basePackageClasses).map(c -> c.getPackage().getName()).toArray(String[]::new));
       
       return em;
    }
    
    @Bean
    public PlatformTransactionManager txManager() {
        return new JpaTransactionManager(entityManagerFactory().getObject());
    }
    
    @Bean
    public TransactionTemplate transactionTemplate() {
        return new TransactionTemplate(txManager());
    }
}