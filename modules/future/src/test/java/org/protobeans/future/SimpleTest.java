package org.protobeans.future;

import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class SimpleTest {
    @Spy
    private ForkJoinPooledComputation<String, String> computation;
    
    @Before 
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void shouldWork() throws InterruptedException, ExecutionException {
        UnboundedCacheableComputation<String, String> cacheableComputation = new UnboundedCacheableComputation<>(computation);
        
        String key = "1";
        
        Mockito.when(computation.computeInternal(key)).thenReturn(key);
        
        cacheableComputation.compute(key).get();
        cacheableComputation.compute(key).get();
        
        Mockito.verify(computation, Mockito.times(1)).computeInternal(key);
    }
}
