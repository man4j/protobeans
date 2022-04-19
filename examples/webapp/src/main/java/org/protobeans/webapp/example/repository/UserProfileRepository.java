package org.protobeans.webapp.example.repository;

import org.protobeans.webapp.example.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
    
    UserProfile findByEmail(String email);
    
}
