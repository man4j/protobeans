import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.protobeans.clickhouse.annotation.EnableClickHouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.support.TransactionTemplate;

import model.clickhouse.CodeStatsRecord;
import model.crdb.CrdbCode;
import model.pg.PgCode;
import repository.crdb.CrdbCodeRepository;
import repository.pg.PgCodeRepository;

@Configuration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes=InsertTest.class)
@DirtiesContext
@Import({CrdbConfig.class, PgConfig.class})
@EnableClickHouse(dbHost = "172.17.0.1", dbPort = "8123")
public class InsertTest {
    @Autowired
    private CrdbCodeRepository crdbCodeRepository;
    
    @Autowired
    private PgCodeRepository pgCodeRepository;

    @Autowired
    private TransactionTemplate crdbTransactionTemplate;
    
    @Autowired
    private TransactionTemplate pgTransactionTemplate;
    
    @Autowired
    private DataSource clickHouseDataSource;
    
    @Test
    @Rollback(false)
    public void shouldWork() throws Exception {
        crdbTransactionTemplate.execute(t -> crdbCodeRepository.save(new CrdbCode(UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString())));
        
        pgTransactionTemplate.execute(t -> pgCodeRepository.save(new PgCode(UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString())));
        
        List<BeanPropertySqlParameterSource> sources = new ArrayList<>();
        
        CodeStatsRecord codeStatsRecord = new CodeStatsRecord(UUID.randomUUID().toString(), 
                                                              UUID.randomUUID().toString(),
                                                              new Random().nextInt(1000),
                                                              new Random().nextInt(1000), 
                                                              new Random().nextInt(1000),
                                                              new Random().nextInt(1000), 
                                                              new Random().nextInt(1000), 
                                                              UUID.randomUUID().toString(), 
                                                              UUID.randomUUID().toString(), 
                                                              UUID.randomUUID().toString(), 
                                                              UUID.randomUUID().toString(), 
                                                              UUID.randomUUID().toString(), 
                                                              UUID.randomUUID().toString(), 
                                                              System.currentTimeMillis());
        
        sources.add(new BeanPropertySqlParameterSource(codeStatsRecord));
        
        new SimpleJdbcInsert(clickHouseDataSource).withTableName("stats_codes").executeBatch(sources.toArray(new BeanPropertySqlParameterSource[] {}));
    }
}
