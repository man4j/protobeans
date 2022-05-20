package org.protobeans.postgresql.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class ProtobeansNamingStrategy extends PhysicalNamingStrategyStandardImpl {
    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        return context.getIdentifierHelper().toIdentifier(name.getText().replaceAll("((?!^)[^_])([A-Z])", "$1_$2").toLowerCase() + "s", name.isQuoted());
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        return context.getIdentifierHelper().toIdentifier(name.getText().replaceAll("((?!^)[^_])([A-Z])", "$1_$2").toLowerCase(),name.isQuoted());
    }
}
