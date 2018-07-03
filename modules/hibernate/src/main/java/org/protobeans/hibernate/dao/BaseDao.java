package org.protobeans.hibernate.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

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
        
    /**
     * This method always generate SQL UPDATE statement.
     */
    public T update(T detachedEntity) {
        em.unwrap(Session.class).update(detachedEntity);
        
        return detachedEntity;
    }
    
    public List<T> list(List<String> ids) {
        return em.unwrap(Session.class).byMultipleIds(entityClass).multiLoad(ids);
    }
    
    public List<T> list(Set<String> ids) {
        return em.unwrap(Session.class).byMultipleIds(entityClass).multiLoad(List.copyOf(ids));
    }
    
    public void delete(T entity) {
        em.remove(entity);
    }
}