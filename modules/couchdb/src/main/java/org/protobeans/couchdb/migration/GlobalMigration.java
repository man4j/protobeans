package org.protobeans.couchdb.migration;

public interface GlobalMigration {
    int getVersion();
    
    void migrate();
}
