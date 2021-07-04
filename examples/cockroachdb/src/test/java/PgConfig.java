import org.protobeans.postgresql.annotation.EnablePostgreSql;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnablePostgreSql(schema= "postgres",
                  dbHost = "172.17.0.1",
                  dbPort = "15432",
                  user = "postgres",
                  password = "postgres",
                  dialect = "org.hibernate.dialect.PostgreSQL10Dialect",
                  basePackages = {"model.pg", "repository.pg"})
public class PgConfig {
    //empty
}
