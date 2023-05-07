package org.protobeans.postgresql.repository;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import jakarta.persistence.EntityManager;

public class ProtobeansJpaRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements ProtobeansJpaRepository<T, ID> {
    private final EntityManager entityManager;

    ProtobeansJpaRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
      super(entityInformation, entityManager);
      this.entityManager = entityManager;
    }

    @Override
    public <S extends T> S insert(S entity) {
        entityManager.persist(entity);
        return entity;
    }
    
    /**
     * This method generate SQL SELECT for fetch and compare detached object with persistent state and generate 
     * SQL update if detached object not equals persistent state.
     */
    @Override
    public <S extends T> S checkAndUpdate(S detachedEntity) {
        return entityManager.merge(detachedEntity);
    }
}
