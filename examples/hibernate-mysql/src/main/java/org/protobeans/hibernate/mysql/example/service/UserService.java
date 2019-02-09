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
    public void insert(User u) {
        userDao.insert(u);
    }
    
    @WithTransaction
    public void update(User u) {
        userDao.update(u);
    }
    
    @WithTransaction
    public User checkAndUpdate(User u) {
        return userDao.checkAndUpdate(u);
    }
    
    @WithTransaction(readOnly = true)
    public long count() {
        return userDao.count();
    }
}