package org.protobeans.jdbc.mysql.example;

import org.protobeans.core.EntryPoint;
import org.protobeans.jdbc.annotation.EnableJdbc;
import org.protobeans.jdbc.annotation.WithTransaction;
import org.protobeans.mysql.annotation.EnableMySql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;

@EnableJdbc
@EnableMySql(dbUrl = "jdbc:mysql://localhost:3306", schema="db11", user = "root", password = "root")
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
        jdbcTemplate.execute("INSERT INTO USERS(id, email, password, confirm_uuid, confirmed) VALUES(33, 'email1', 'password1', 'uuid1', 1)");
        jdbcTemplate.execute("INSERT INTO USERS(id, email, password, confirm_uuid, confirmed) VALUES(44, 'email2', 'password2', 'uuid2', 1)");
    }
}
