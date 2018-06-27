package org.protobeans.security.service;

import org.protobeans.security.model.AbstractProfile;

public interface ProfileService {
    AbstractProfile getById(String id);
    
    AbstractProfile update(AbstractProfile profile);
}
