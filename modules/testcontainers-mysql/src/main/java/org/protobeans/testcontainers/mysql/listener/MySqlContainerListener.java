package org.protobeans.testcontainers.mysql.listener;

import org.protobeans.testcontainers.mysql.annotation.EnableMySqlContainer;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.testcontainers.containers.MySQLContainer;

public class MySqlContainerListener extends AbstractTestExecutionListener {
    private MySQLContainer<?> mysql;
    
    private final String DB_SCHEMA = "test_db";
    
    private String dbUrl;
    
    @Override
    @SuppressWarnings("rawtypes")
    public void beforeTestClass(TestContext testContext) throws Exception {
        EnableMySqlContainer annotation = testContext.getTestClass().getAnnotation(EnableMySqlContainer.class);
        
        mysql = new MySQLContainer() {
            @Override
            public String getJdbcUrl() {
                dbUrl = "jdbc:mysql://" + getContainerIpAddress() + ":" + getMappedPort(3306);
                
                return dbUrl + "/" + DB_SCHEMA;
            }
            
            @Override
            protected void configure() {
//                optionallyMapResourceParameterAsVolume("TC_MY_CNF", "/etc/mysql/conf.d", "mysql-default-conf"); Exception in Jenkins run

                addExposedPort(3306);
                
                addEnv("MYSQL_DATABASE", "test_db");
                addEnv("MYSQL_USER", annotation.user());
                addEnv("MYSQL_PASSWORD", annotation.password());
                addEnv("MYSQL_ROOT_PASSWORD", annotation.password());
                
                setCommand("mysqld");
                
                setStartupAttempts(3);        
            }
        };
        
        mysql.start();
        
        exposeSystemVars(annotation);  
    }

    private void exposeSystemVars(EnableMySqlContainer annotation) {
        System.setProperty(annotation.exposeUserAs(), annotation.user());  
        System.setProperty(annotation.exposePasswordAs(), annotation.password());  
        System.setProperty(annotation.exposeUrlAs(), dbUrl);
        System.setProperty(annotation.exposeSchemaAs(), DB_SCHEMA);
    }
    
    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        mysql.stop();
    }
}
