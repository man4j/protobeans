package org.protobeans.security.service;

import org.protobeans.security.model.AbstractProfile;

public interface ProfileService {
    AbstractProfile getById(String id);
    
    default AbstractProfile getByLogin(String login) {
        return null;
    };
    
    AbstractProfile update(AbstractProfile profile);
}
