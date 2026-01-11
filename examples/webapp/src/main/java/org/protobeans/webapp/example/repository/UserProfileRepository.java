package org.protobeans.webapp.example.repository;

import org.protobeans.postgresql.repository.ProtobeansJpaRepository;
import org.protobeans.webapp.example.entity.UserProfile;

public interface UserProfileRepository extends ProtobeansJpaRepository<UserProfile, String> {
    
    UserProfile findByEmail(String email);
    
    UserProfile findByConfirmUuid(String token);
    
}
