package org.protobeans.couchdb.migration;

public interface Migration {
    int getVersion();
    
    String getServiceName();

    void migrate();
}
