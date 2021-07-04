import org.protobeans.cockroachdb.annotation.EnableCockroachDb;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCockroachDb(schema= "testdb",
                   dbHosts = "172.17.0.1",
                   dbPorts = "5432",
                   dialect = "org.hibernate.dialect.CockroachDB201Dialect",
                   basePackages = {"model.crdb", "repository.crdb"})
public class CrdbConfig {
    //empty
}
