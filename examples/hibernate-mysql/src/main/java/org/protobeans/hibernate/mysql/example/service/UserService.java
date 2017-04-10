package org.protobeans.hibernate.mysql.example.service;

import org.protobeans.hibernate.annotation.WithTransaction;
import org.protobeans.hibernate.mysql.example.dao.UserDao;
import org.protobeans.hibernate.mysql.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;
    
    @WithTransaction
    public void saveOrUpdate(User u) {
        userDao.saveOrUpdate(u);
    }
    
    @WithTransaction
    public void update(User u) {
        userDao.update(u);
    }
    
    @WithTransaction
    public User merge(User u) {
        return userDao.checkAndUpdate(u);
    }
    
    @WithTransaction
    public void insertUsers(int count) {
        for (int i = 0; i < count; i++) {
            userDao.saveOrUpdate(new User("user" + i + "@mail.com", "pw" + i));
        }
    }
    
    @WithTransaction(readOnly = true)
    public long count() {
        return userDao.count();
    }
}