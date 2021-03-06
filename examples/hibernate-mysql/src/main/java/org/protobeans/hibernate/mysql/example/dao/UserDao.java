package org.protobeans.hibernate.mysql.example.dao;

import org.springframework.stereotype.Repository;
import org.protobeans.hibernate.dao.BaseDao;
import org.protobeans.hibernate.mysql.example.model.User;

@Repository
public class UserDao extends BaseDao<User> {
    public long count() {
        return em.createQuery("SELECT count(*) FROM User", Long.class).getSingleResult();
    }
}
