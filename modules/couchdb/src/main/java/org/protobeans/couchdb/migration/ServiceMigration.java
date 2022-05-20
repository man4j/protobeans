package org.protobeans.couchdb.migration;

public interface ServiceMigration {
    int getVersion();
    
    void migrate();
}
