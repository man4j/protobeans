package org.protobeans.hibernate.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.springframework.core.ResolvableType;

abstract public class BaseDao<T> {
    @PersistenceContext
    protected EntityManager em;
    
    private Class<T> entityClass;
    
    @SuppressWarnings("unchecked")
    public BaseDao() {
        entityClass = (Class<T>) ResolvableType.forClass(BaseDao.class, this.getClass()).resolveGeneric(0);
    }
    
    /**
     * This method always generate SQL INSERT statement.
     */
    public void insert(T entity) {
        em.persist(entity);
    }
    
    /**
     * This method generate SQL SELECT statement if entity not found in first-level cache or second-level cache;
     */
    public T get(Serializable id) {
        return em.find(entityClass, id);
    }
    
    public T getReference(Serializable id) {
        return em.getReference(entityClass, id);
    }
    
    /**
     * This method generate SQL SELECT for fetch and compare detached object with persistent state and generate 
     * SQL update if detached object not equals persistent state.
     */
    public T checkAndUpdate(T detachedEntity) {
        return em.merge(detachedEntity);
    }
    
    public T saveOrUpdate(T detachedEntity) {
        em.unwrap(Session.class).saveOrUpdate(detachedEntity);
        
        return detachedEntity;
    }
    
    public T update(T detachedEntity) {
        em.unwrap(Session.class).update(detachedEntity);
        
        return detachedEntity;
    }
    
    public void delete(T entity) {
        em.remove(entity);
    }
}