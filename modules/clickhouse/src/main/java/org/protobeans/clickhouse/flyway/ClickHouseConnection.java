package org.protobeans.clickhouse.flyway;

import java.sql.SQLException;

import org.flywaydb.core.internal.database.base.Connection;
import org.flywaydb.core.internal.database.base.Schema;

public class ClickHouseConnection extends Connection<ClickHouseDatabase> {
    protected ClickHouseConnection(ClickHouseDatabase database, java.sql.Connection connection) {
        super(database, connection);
    }

    @Override
    protected String getCurrentSchemaNameOrSearchPath() throws SQLException {
        return getJdbcConnection().getCatalog();
    }

    @Override
    public Schema getSchema(String name) {
        return new ClickHouseSchema(jdbcTemplate, database, name);
    }
}
