import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.protobeans.cockroachdb.transaction.RetryableTransactionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import model.CrdbCode;
import repository.CrdbCodeRepository;

@Configuration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes=InsertTest.class)
@DirtiesContext
@Import({CrdbConfig.class})
public class InsertTest {
    @Autowired
    private CrdbCodeRepository crdbCodeRepository;
    
    @Autowired
    private RetryableTransactionTemplate transactionTemplate;
    
    @Test
    public void shouldWork() throws Exception {
        transactionTemplate.execute(t -> crdbCodeRepository.save(new CrdbCode("1", "1", "1")));

        List<Thread> threads = new ArrayList<>();
        
        for (int i = 0; i < 10; i++) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    transactionTemplate.executeWithoutResult(t -> {
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
}