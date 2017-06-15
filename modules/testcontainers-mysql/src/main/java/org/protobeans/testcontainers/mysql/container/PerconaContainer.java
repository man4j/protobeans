package org.protobeans.testcontainers.mysql.container;

import org.testcontainers.containers.JdbcDatabaseContainer;

public class PerconaContainer<SELF extends PerconaContainer<SELF>> extends JdbcDatabaseContainer<SELF> {
    private static final int PERCONA_PORT = 3306;
    private static final String PERCONA_USER = "root";
    
    private final String rootPassword;
    
    public PerconaContainer(String schema, String rootPassword, boolean skipInit, String imageTag) {
        super("imagenarium/percona-master:" + imageTag);

        this.rootPassword = rootPassword;
        
        addExposedPort(PERCONA_PORT);
        
        addEnv("MYSQL_DATABASE", schema);
        addEnv("MYSQL_ROOT_PASSWORD", rootPassword);
        addEnv("PXC_STRICT_MODE", "MASTER");
        
        if (skipInit) {
            addEnv("SKIP_INIT", "true");
        }
    }

    @Override
    protected String getDriverClassName() {
        return "com.mysql.jdbc.Driver";
    }

    @Override
    public String getJdbcUrl() {
        return "jdbc:mysql://" + getContainerIpAddress() + ":" + getMappedPort(PERCONA_PORT);
    }

    @Override
    public String getUsername() {
        return PERCONA_USER;
    }

    @Override
    public String getPassword() {
        return rootPassword;
    }

    @Override
    protected String getTestQueryString() {
        return "SELECT 1";
    }

    @Override
    protected Integer getLivenessCheckPort() {
        return getMappedPort(PERCONA_PORT);
    }
}
