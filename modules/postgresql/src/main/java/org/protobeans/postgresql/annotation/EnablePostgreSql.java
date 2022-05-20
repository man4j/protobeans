package org.protobeans.postgresql.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.protobeans.postgresql.config.PostgreSqlConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(PostgreSqlConfig.class)
@EnableJpaRepositories(considerNestedRepositories = true, enableDefaultTransactions = false, entityManagerFactoryRef = "pgEntityManager", transactionManagerRef = "pgTransactionManager")
@Configuration
public @interface EnablePostgreSql {
    String dbHost();
    
    String dbPort() default "5432";
    
    String schema();
    
    String user();
    
    String password();
    
    String maxPoolSize() default "auto"; 
    
    String transactionIsolation() default "TRANSACTION_READ_COMMITTED";
    
    boolean reindexOnStart() default false;
    
    boolean disablePreparedStatements() default false;
    
    String showSql() default "false";
    
    /**
     * Also do not forget set log level for org.hibernate.stat to DEBUG
     */
    String enableStatistics() default "false";
        
    @AliasFor(annotation = EnableJpaRepositories.class, attribute = "basePackages")
    String[] basePackages();
    
    int batchSize() default 1_000;
    
    int fetchSize() default 1_00;
    
    String migrationsPath() default "postgres/migrations";
}