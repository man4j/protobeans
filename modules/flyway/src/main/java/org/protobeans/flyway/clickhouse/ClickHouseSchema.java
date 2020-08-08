package org.protobeans.flyway.clickhouse;

import java.sql.SQLException;
import java.util.List;

import org.flywaydb.core.internal.database.base.Schema;
import org.flywaydb.core.internal.database.base.Table;
import org.flywaydb.core.internal.jdbc.JdbcTemplate;

public class ClickHouseSchema extends Schema<ClickHouseDatabase, ClickHouseTable> {
    public ClickHouseSchema(JdbcTemplate jdbcTemplate, ClickHouseDatabase database, String name) {
        super(jdbcTemplate, database, name);
    }

    @Override
    protected boolean doExists() throws SQLException {
        return jdbcTemplate.queryForInt("SELECT COUNT(*) FROM system.databases WHERE name = ?", name) > 0;
    }

    @Override
    protected boolean doEmpty() throws SQLException {
        return jdbcTemplate.queryForInt("SELECT COUNT(*) FROM system.tables WHERE database = ?", name) == 0;
    }

    @Override
    protected void doCreate() throws SQLException {
        jdbcTemplate.executeStatement("CREATE DATABASE " + database.quote(name));
    }

    @Override
    protected void doDrop() throws SQLException {
        if (jdbcTemplate.getConnection().getCatalog().equals(name)) {
            jdbcTemplate.getConnection().setCatalog("default");
        }
        
        jdbcTemplate.executeStatement("DROP DATABASE " + database.quote(name));
    }

    @Override
    protected void doClean() throws SQLException {
        for (Table table : allTables()) {
            table.drop();
        }
    }

    @Override
    protected ClickHouseTable[] doAllTables() throws SQLException {
        List<String> tableNames = jdbcTemplate.queryForStringList("SELECT name FROM system.tables WHERE database = ?", name);
        
        ClickHouseTable[] result = new ClickHouseTable[tableNames.size()];
        
        for (int i = 0; i < tableNames.size(); i++) {
            result[i] = new ClickHouseTable(jdbcTemplate, database, this, tableNames.get(i));
        }
        
        return result;
    }

    @Override
    public Table getTable(String tableName) {
        return new ClickHouseTable(jdbcTemplate, database, this, tableName);
    }
}
