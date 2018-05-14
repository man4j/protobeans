package org.protobeans.security.service;

import org.protobeans.security.model.AbstractProfile;

public interface ProfileService {
    AbstractProfile getByEmail(String email);
    
    AbstractProfile update(AbstractProfile profile);
}
