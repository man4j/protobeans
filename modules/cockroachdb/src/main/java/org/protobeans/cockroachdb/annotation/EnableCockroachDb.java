package org.protobeans.cockroachdb.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.protobeans.cockroachdb.config.CockroachDbConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(CockroachDbConfig.class)
@EnableJpaRepositories(considerNestedRepositories = true, enableDefaultTransactions = false, entityManagerFactoryRef = "crdbEntityManager", transactionManagerRef = "crdbTransactionManager")
@Configuration
public @interface EnableCockroachDb {
    String dbHosts();
    
    String dbPorts() default "5432";
    
    String schema();
    
    String maxPoolSize() default "auto"; 
    
    String showSql() default "false";
    
    String dialect();
    
    /**
     * Also do not forget set log level for org.hibernate.stat to DEBUG
     */
    String enableStatistics() default "false";
        
    @AliasFor(annotation = EnableJpaRepositories.class, attribute = "basePackages")
    String[] basePackages();
    
    int batchSize() default 1_000;
    
    String migrationsPath() default "cockroachdb/migrations";
}