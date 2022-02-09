package org.protobeans.couchdb.migration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MigrationService {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Autowired
    private MigrationDb migrationDb;

    @Autowired(required = false)
    private List<ServiceMigration> serviceMigrations = new ArrayList<>();
    
    @Autowired(required = false)
    private List<GlobalMigration> globalMigrations = new ArrayList<>();

    @PostConstruct
    public void migrate() {
        logger.info("Found migrations: {}", serviceMigrations.size());
        
        String versionDocName = System.getProperty("service.name") != null ? System.getProperty("service.name") : "version"; 

        if (!serviceMigrations.isEmpty()) {
            Map<String, Object> versionDoc = migrationDb.getRaw(versionDocName);

            int dbVersion = versionDoc == null ? 0 : (int)versionDoc.get("version");
            
            if (versionDoc == null) {
            	versionDoc = new HashMap<>();
            	versionDoc.put("_id", versionDocName);
            }

            logger.info("Current db version for service migrations: {}", dbVersion);
            
            for (ServiceMigration m : serviceMigrations.stream().sorted(Comparator.comparing(ServiceMigration::getVersion)).filter(m -> m.getVersion() > dbVersion).collect(Collectors.toList())) {
            	logger.info("CouchDB service migration started. Version: {}", m.getVersion());
            	
                m.migrate();
                
                versionDoc.put("version", m.getVersion());
                migrationDb.saveOrUpdate(versionDoc);

                logger.info("CouchDB service migration finished. Version: {}", m.getVersion());
            }
        }
        
        if (!globalMigrations.isEmpty()) {
            Map<String, Object> versionDoc = migrationDb.getRaw("global");

            int dbVersion = versionDoc == null ? 0 : (int)versionDoc.get("version");
            
            if (versionDoc == null) {
                versionDoc = new HashMap<>();
                versionDoc.put("_id", "global");
            }

            logger.info("Current db version for global migrations: {}", dbVersion);
            
            for (GlobalMigration m : globalMigrations.stream().sorted(Comparator.comparing(GlobalMigration::getVersion)).filter(m -> m.getVersion() > dbVersion).collect(Collectors.toList())) {
                logger.info("CouchDB global migration started. Version: {}", m.getVersion());
                
                m.migrate();
                
                versionDoc.put("version", m.getVersion());
                migrationDb.saveOrUpdate(versionDoc);

                logger.info("CouchDB global migration finished. Version: {}", m.getVersion());
            }
        }
    }
}
