package org.protobeans.couchdb.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.protobeans.couchdb.model.CouchDbMigration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

abstract public class CouchDbMigrationService {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Autowired(required = false)
    private List<CouchDbMigration> migrations = new ArrayList<>();

    @PostConstruct
    public void migrate() {
        logger.info("Found migrations: {}", migrations.size());
        
        int dbVersion = getVersion() == null ? 0 : getVersion();
            
        for (CouchDbMigration m : migrations.stream().sorted(Comparator.comparing(CouchDbMigration::getVersion)).filter(m -> m.getVersion() > dbVersion).collect(Collectors.toList())) {
            logger.info("CouchDB migration started. Version: {}", m.getVersion());
            
            m.migrate();

            saveVersion(m.getVersion());
            
            logger.info("CouchDB migrationV2 finished. Version: {}", m.getVersion());
        }
    }
    
    abstract protected Integer getVersion();
    
    abstract protected void saveVersion(int version);
}
