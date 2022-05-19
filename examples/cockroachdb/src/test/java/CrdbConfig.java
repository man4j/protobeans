import org.protobeans.cockroachdb.annotation.EnableCockroachDb;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCockroachDb(schema= "testdb",
                   dbHosts = "172.17.0.1",
                   dbPorts = "5432",
                   maxPoolSize = "16",
                   basePackages = {"model", "repository"})
public class CrdbConfig {
    //empty
}