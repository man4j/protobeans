package org.protobeans.hibernate.mysql.example;

import javax.annotation.PostConstruct;

import org.hibernate.dialect.MySQL57InnoDBDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.protobeans.core.EntryPoint;
import org.protobeans.hibernate.annotation.EnableHibernate;
import org.protobeans.hibernate.annotation.WithTransaction;
import org.protobeans.hibernate.mysql.example.dao.UserDao;
import org.protobeans.hibernate.mysql.example.model.User;
import org.protobeans.mysql.annotation.EnableMySql;

@EnableMySql(dbUrl = "jdbc:mysql://localhost:3306/dev_db", user = "root", password = "s:password")
@EnableHibernate(showSql = "true", basePackageClasses = User.class, dialect = MySQL57InnoDBDialect.class)
@ComponentScan(basePackageClasses = UserDao.class)
public class Main {
    @Autowired
    private UserService userService;
    
    @Bean
    UserService userService() {
        return new UserService();
    }
    
    @PostConstruct
    void start() {
        userService.insertUsers();
    }
    
    public static void main(String[] args) {
        EntryPoint.run(Main.class);
    }
}

class UserService {
    @Autowired
    private UserDao userDao;
    
    @WithTransaction
    public void insertUsers() {
        userDao.saveOrUpdate(new User("user1@ya.ru", "pw1"));
        userDao.saveOrUpdate(new User("user2@ya.ru", "pw2"));
    }
}
