package org.protobeans.flyway.clickhouse;

import java.sql.SQLException;

import org.flywaydb.core.internal.database.base.Table;
import org.flywaydb.core.internal.jdbc.JdbcTemplate;

public class ClickHouseTable extends Table<ClickHouseDatabase, ClickHouseSchema> {
    public ClickHouseTable(JdbcTemplate jdbcTemplate, ClickHouseDatabase database, ClickHouseSchema schema, String name) {
        super(jdbcTemplate, database, schema, name);
    }

    @Override
    protected boolean doExists() throws SQLException {
        return jdbcTemplate.queryForInt("SELECT COUNT(*) FROM system.tables WHERE database = ? AND name = ?", schema.getName(), name) > 0;
    }

    @Override
    protected void doLock() throws SQLException {
        // empty
    }

    @Override
    protected void doDrop() throws SQLException {
        jdbcTemplate.executeStatement("DROP TABLE " + database.quote(schema.getName(), name));
    }
}
