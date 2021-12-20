package org.protobeans.couchdb.migration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.protobeans.core.util.PropsUtil;
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
    private List<Migration> migrations = new ArrayList<>();

    @PostConstruct
    public void migrate() {
        logger.info("Found migrations: {}", migrations.size());

        if (!migrations.isEmpty()) {
            Map<String, Object> versionDoc = migrationDb.getRaw("version");

            int dbVersion = versionDoc == null ? 0 : (int)versionDoc.get("version");
            
            if (versionDoc == null) {
            	versionDoc = new HashMap<>();
            }

            logger.info("Current db version: {}", dbVersion);
            
            String serviceName = PropsUtil.getProp("SERVICE_NAME");

            for (Migration m : migrations.stream().sorted(Comparator.comparing(Migration::getVersion)).filter(m -> m.getVersion() > dbVersion).collect(Collectors.toList())) {
            	if (serviceName == null || (serviceName != null && serviceName.equals(m.getServiceName()))) {            	
	            	logger.info("CouchDB migration started. Version: {}", m.getVersion());
	            	
	                m.migrate();
	                
	                versionDoc.put("version", m.getVersion());
	                migrationDb.saveOrUpdate(versionDoc);
	
	                logger.info("CouchDB Migration finished. Version: {}", m.getVersion());
            	}                
            }
        }
    }
}
