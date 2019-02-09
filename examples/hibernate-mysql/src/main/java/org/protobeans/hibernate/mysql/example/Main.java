package org.protobeans.hibernate.mysql.example;

import java.util.UUID;

import org.protobeans.core.EntryPoint;
import org.protobeans.flyway.annotation.EnableFlyway;
import org.protobeans.hibernate.annotation.EnableHibernate;
import org.protobeans.hibernate.mysql.example.dao.UserDao;
import org.protobeans.hibernate.mysql.example.model.User;
import org.protobeans.hibernate.mysql.example.service.UserService;
import org.protobeans.mysql.annotation.EnableMySql;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.orm.jpa.vendor.Database;

@EnableMySql(dbHost = "s:host", schema = "s:schema", user = "root", password = "s:password")
@EnableFlyway
@EnableHibernate(showSql = "true", dialect = Database.MYSQL, basePackages = "org.protobeans.hibernate.mysql.example")
@ComponentScan(basePackageClasses = {UserDao.class, UserService.class})
public class Main {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext ctx = EntryPoint.run(Main.class)) {
            UserService userService = ctx.getBean(UserService.class);
            
            User u = new User(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6) + "@example.com", 
                              UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6));
            
            userService.insert(u);
            
            u.setEmail(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6) + "@example.com");
            
            userService.update(u);
            
            u.setEmail(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6) + "@example.com");
            
            userService.checkAndUpdate(u);
        }
    }
}
