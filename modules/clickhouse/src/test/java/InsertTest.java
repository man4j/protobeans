import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.protobeans.clickhouse.annotation.EnableClickHouse;
import org.protobeans.flyway.annotation.EnableFlyway;
import org.protobeans.jdbc.annotation.EnableJdbc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ru.yandex.clickhouse.ClickHouseDataSource;

@Configuration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes=InsertTest.class)
@DirtiesContext
@EnableClickHouse(dbHost = "172.17.0.1", dbPort = "8123")
@EnableFlyway(repair = false)
@EnableJdbc
public class InsertTest {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    @Autowired
    private JdbcTemplate jdbcTemplate; 
    
    @Autowired
    private ClickHouseDataSource clickHouseDataSource;
    
    @Test
    public void shouldWork() throws Exception {
        optimize();

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
        
        Assertions.assertEquals(1, getStats(0, Long.MAX_VALUE, 1, 0).size());
    }
    
    public void optimize() throws Exception {
        jdbcTemplate.execute("OPTIMIZE TABLE stats_codes");//for deduplication
        jdbcTemplate.execute("OPTIMIZE TABLE stats_codes_incoming");//for deduplication
        jdbcTemplate.execute("OPTIMIZE TABLE stats_codes_outgoing");//for deduplication
    }
    
    public List<CodeStatsRecord> getStats(long startDate, long endDate, long limit, long offset) {
        return namedParameterJdbcTemplate.query("SELECT * FROM stats_codes_outgoing "
                                              + "WHERE create_time >= :startDate AND create_time < :endDate "
                                              + "ORDER BY create_time DESC LIMIT :limit OFFSET :offset", Map.of("startDate", startDate / 1_000, 
                                                                                                                "endDate", endDate / 1_000, 
                                                                                                                "limit", limit, 
                                                                                                                "offset", offset), new BeanPropertyRowMapper<>(CodeStatsRecord.class));
    }
}
