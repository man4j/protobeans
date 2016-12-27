package org.protobeans.jdbc.mysql.example;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.protobeans.core.EntryPoint;
import org.protobeans.jdbc.annotation.EnableJdbc;
import org.protobeans.jdbc.annotation.WithTransaction;
import org.protobeans.mysql.annotation.EnableMySql;

@EnableJdbc
@EnableMySql(dbUrl = "jdbc:mysql://localhost:3306/dev_db", user = "root", password = "s:password")
public class Main {
    @Autowired
    private UserService userService;
    
    @Bean
    UserService userService() {
        return new UserService();
    }
    
    @EventListener(ContextRefreshedEvent.class)
    void start() {
        userService.insertUsers();
    }
    
    public static void main(String[] args) {
        EntryPoint.run(Main.class);
    }
}

class UserService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @WithTransaction
    public void insertUsers() {
        jdbcTemplate.execute("INSERT INTO USERS(email, password, confirm_uuid, confirmed) VALUES('email1', 'password1', 'uuid1', 1)");
        jdbcTemplate.execute("INSERT INTO USERS(email, password, confirm_uuid, confirmed) VALUES('email2', 'password2', 'uuid2', 1)");
    }
}
