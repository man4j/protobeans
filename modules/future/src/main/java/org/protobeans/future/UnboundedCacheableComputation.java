package org.protobeans.future;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class UnboundedCacheableComputation<K, V> implements Computation<K, V> {
    private final ConcurrentHashMap<K, CompletableFuture<V>> cache = new ConcurrentHashMap<>();
    
    private final Computation<K, V> delegate;
    
    public UnboundedCacheableComputation(Computation<K, V> delegate) {
        this.delegate = delegate;
    }

    @Override
    public CompletableFuture<V> compute(K k, Function<K, V> f) {
        if (cache.containsKey(k)) return cache.get(k);
        
        CompletableFuture<V> future = new CompletableFuture<>();
            
        return Optional.ofNullable(cache.putIfAbsent(k, future))
                       .orElse(delegate.compute(k, f).whenComplete((v, e) ->  {
                           if (e != null) {
                               future.completeExceptionally(e);
                           } else {
                               future.complete(v);
                           }
                       }));
    }  
}
