package org.protobeans.future;

import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class SimpleTest {
    @Spy
    private ForkJoinPooledComputation<String, String> computation;
    
    @BeforeAll
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void shouldWork() throws InterruptedException, ExecutionException {
        UnboundedCacheableComputation<String, String> cacheableComputation = new UnboundedCacheableComputation<>(computation);
        
        String key = "1";
        
        Function<String, String> computeFunction = k -> k + k;
        
        cacheableComputation.compute(key, computeFunction).get();
        cacheableComputation.compute(key, computeFunction).get();
        
        Mockito.verify(computation, Mockito.times(1)).compute(key, computeFunction);
    }
}
