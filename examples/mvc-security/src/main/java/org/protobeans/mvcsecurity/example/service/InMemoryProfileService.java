package org.protobeans.mvcsecurity.example.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.protobeans.mvcsecurity.example.model.UserProfile;
import org.protobeans.security.model.AbstractProfile;
import org.protobeans.security.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class InMemoryProfileService implements ProfileService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    private Map<String, UserProfile> byEmail = new ConcurrentHashMap<>();
    
    public InMemoryProfileService() {
        UserProfile p = new UserProfile();
        
        p.setConfirmed(true);
        p.setEmail("man4j@ya.ru");
//        p.setPassword(passwordEncoder.encode("123456"));
        p.setUserName("Vladimir");
        
        byEmail.put(p.getEmail(), p);
    }

    @Override
    public UserProfile getByEmail(String email) {
        return null;
    }

    @Override
    public UserProfile getByConfirmUuid(String uuid) {
        throw new UnsupportedOperationException();
    }

    @Override
    public UserProfile update(AbstractProfile profile) {
        throw new UnsupportedOperationException();
    }

    @Override
    public UserProfile create(String email, String password, boolean confirmed) {
        throw new UnsupportedOperationException();
    }
}
