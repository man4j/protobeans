package org.protobeans.clickhouse.flyway;

import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.internal.parser.Parser;
import org.flywaydb.core.internal.parser.ParsingContext;

public class ClickHouseParser extends Parser {
    public ClickHouseParser(Configuration configuration, ParsingContext parsingContext) {
        super(configuration, parsingContext, 3);
    }
}