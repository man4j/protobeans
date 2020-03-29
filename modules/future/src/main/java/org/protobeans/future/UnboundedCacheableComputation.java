package org.protobeans.future;

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
        return cache.computeIfAbsent(k, _k -> delegate.compute(_k, f));
    }  
}
