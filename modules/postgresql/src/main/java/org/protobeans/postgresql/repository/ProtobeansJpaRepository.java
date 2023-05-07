package org.protobeans.postgresql.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProtobeansJpaRepository<T, ID> extends JpaRepository<T, ID> {    
    /**
     * This method always generate SQL INSERT statement.
     */
    <S extends T> S insert(S entity);
    
    /**
     * This method generate SQL SELECT for fetch and compare detached object with persistent state and generate 
     * SQL update if detached object not equals persistent state.
     */
    T checkAndUpdate(T detachedEntity);
}
