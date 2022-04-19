package org.protobeans.couchdb.model;

public interface CouchDbMigration {
    int getVersion();
    
    void migrate();
}
