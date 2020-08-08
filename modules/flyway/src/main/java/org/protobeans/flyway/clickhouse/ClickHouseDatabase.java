package org.protobeans.flyway.clickhouse;

import java.sql.Connection;

import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.internal.database.base.Database;
import org.flywaydb.core.internal.database.base.Table;
import org.flywaydb.core.internal.jdbc.JdbcConnectionFactory;

public class ClickHouseDatabase extends Database<ClickHouseConnection> {
    public ClickHouseDatabase(Configuration configuration, JdbcConnectionFactory jdbcConnectionFactory) {
        super(configuration, jdbcConnectionFactory);
    }

    @Override
    protected ClickHouseConnection doGetConnection(Connection connection) {
        return new ClickHouseConnection(this, connection);
    }

    @Override
    public void ensureSupported() {
        //empty
    }

    @Override
    public boolean supportsDdlTransactions() {
        return false;
    }

    @Override
    public boolean supportsChangingCurrentSchema() {
        return false;
    }

    @Override
    public String getBooleanTrue() {
        return "1";
    }

    @Override
    public String getBooleanFalse() {
        return "0";
    }

    @Override
    protected String doQuote(String identifier) {
        return "`" + identifier + "`";
    }

    @Override
    public boolean catalogIsSchema() {
        return true;
    }

    @Override
    public String getRawCreateScript(Table table, boolean baseline) {
        return "CREATE TABLE " + table + " (\n" +                
                  "installed_rank Int32,\n" +
                  "version Nullable(String),\n" +
                  "description String,\n" +
                  "type String,\n" +
                  "script String,\n" +
                  "checksum Nullable(Int32),\n" +
                  "installed_by Nullable(String),\n" +
                  "installed_on DateTime,\n" +
                  "execution_time Int32,\n" +
                  "success UInt8\n" +
               ") ENGINE = TinyLog;";
    }
}
