import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.protobeans.clickhouse.annotation.EnableClickHouse;
import org.protobeans.cockroachdb.transaction.RetryableTransactionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.annotation.DirtiesContext;
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
    private RetryableTransactionTemplate retryableTransactionTemplate;
    
    @Autowired
    private TransactionTemplate pgTransactionTemplate;
    
    @Autowired
    private DataSource clickHouseDataSource;
    
    @Test
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
    
    @Test
    public void shouldWork1() throws Exception {
        for (int i = 0; i < 2; i++) {        
            new Thread() {
                @Override
                public void run() {
                    do {                    
                        try {                    
                            crdbTransactionTemplate.executeWithoutResult(t -> {
                                CrdbCode code = crdbCodeRepository.findById("1").get();
                                
                                String orderId = UUID.randomUUID().toString();
                                
                                code.setOrderId(orderId);
                            });
                            
                            break;
                        } catch (CannotAcquireLockException e) {
                            continue;
                        }
                    } while (true);
                }
            }.start();
        }
        
        Thread.sleep(Integer.MAX_VALUE);
    }
    
    @Test
    public void shouldWork2() throws Exception {
        crdbTransactionTemplate.executeWithoutResult(t -> {
            CrdbCode code = crdbCodeRepository.findById("1").get();
            code.setOrderId("0");
        });

        List<Thread> threads = new ArrayList<>();
        
        for (int i = 0; i < 10; i++) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    retryableTransactionTemplate.executeWithoutResult(t -> {
                        CrdbCode code = crdbCodeRepository.findById("1").get();
                        
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        
                        int counter = Integer.parseInt(code.getOrderId());
                        
                        code.setOrderId((++counter) + "");
                    });
                }
            };
            
            threads.add(t);
            t.start();
        }

        for (int i = 0; i < 10; i++) {
            threads.get(i).join();
        }
    }
    
    @Test
    public void shouldWork3() throws InterruptedException {
        List<BeanPropertySqlParameterSource> sources = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                CodeStatsRecord codeStatsRecord = new CodeStatsRecord(i + "",//orderId 
                                                                      "1",
                                                                      1,
                                                                      1, 
                                                                      1,
                                                                      1, 
                                                                      1, 
                                                                      "issuer1", 
                                                                      "issuer1", 
                                                                      "sender1", 
                                                                      "sender1", 
                                                                      "recipient1", 
                                                                      "recipient1", 
                                                                      System.currentTimeMillis());
                Thread.sleep(10);
                
                sources.add(new BeanPropertySqlParameterSource(codeStatsRecord));
            }
        }
        
        new SimpleJdbcInsert(clickHouseDataSource).withTableName("stats_codes").executeBatch(sources.toArray(new BeanPropertySqlParameterSource[] {}));
    }
}
