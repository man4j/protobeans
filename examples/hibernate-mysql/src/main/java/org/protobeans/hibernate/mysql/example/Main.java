package org.protobeans.hibernate.mysql.example;

import org.hibernate.dialect.MySQL57InnoDBDialect;
import org.protobeans.core.EntryPoint;
import org.protobeans.flyway.annotation.EnableFlyway;
import org.protobeans.hibernate.annotation.EnableHibernate;
import org.protobeans.hibernate.mysql.example.dao.UserDao;
import org.protobeans.hibernate.mysql.example.model.User;
import org.protobeans.hibernate.mysql.example.service.UserService;
import org.protobeans.mysql.annotation.EnableMySql;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@EnableMySql(dbUrl = "s:url", user = "s:user", password = "s:password")
@EnableFlyway(dbUrl = "s:url", user = "s:user", password = "s:password")
@EnableHibernate(showSql = "true", basePackageClasses = User.class, dialect = MySQL57InnoDBDialect.class)
@ComponentScan(basePackageClasses = {UserDao.class, UserService.class})
public class Main {
    public static void main(String[] args) {
        System.setProperty("user", "root");
        System.setProperty("password", "root");
        System.setProperty("url", "jdbc:mysql://localhost:3306/test1_db");
        
        try (AnnotationConfigApplicationContext ctx = EntryPoint.run(Main.class)) {
            UserService userService = ctx.getBean(UserService.class);
            
            User u = new User("email", "pwd");
            
            userService.saveOrUpdate(u);
            
            u.setEmail("email1");
            
            userService.update(u);
        }
    }
}
