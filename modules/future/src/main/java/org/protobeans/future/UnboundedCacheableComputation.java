package org.protobeans.future;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class UnboundedCacheableComputation<K, V> implements Computation<K, V> {
    private final ConcurrentHashMap<K, CompletableFuture<V>> cache = new ConcurrentHashMap<>();
    
    private final Computation<K, V> delegate;
    
    public UnboundedCacheableComputation(Computation<K, V> delegate) {
        this.delegate = delegate;
    }

    @Override
    public CompletableFuture<V> compute(K k) {
        if (cache.containsKey(k)) return cache.get(k);
        
        CompletableFuture<V> f = new CompletableFuture<>();
            
        return Optional.ofNullable(cache.putIfAbsent(k, f))
                       .orElse(delegate.compute(k).whenComplete((v, e) ->  {
                           if (e != null) {
                               f.completeExceptionally(e);
                           } else {
                               f.complete(v);
                           }
                       }));
    }  
}
