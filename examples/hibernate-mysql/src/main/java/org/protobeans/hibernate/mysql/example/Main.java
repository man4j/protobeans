package org.protobeans.hibernate.mysql.example;

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
@EnableHibernate(showSql = "true", basePackageClasses = User.class)
@ComponentScan(basePackageClasses = {UserDao.class, UserService.class})
public class Main {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext ctx = EntryPoint.run(Main.class)) {
            UserService userService = ctx.getBean(UserService.class);
            
            User u = new User("email", "pwd");
            
            userService.saveOrUpdate(u);
            
            u.setEmail("email1");
            
            userService.update(u);
        }
    }
}
