package org.protobeans.future;

import java.util.concurrent.CompletableFuture;

abstract public class ForkJoinPooledComputation<K, V> implements Computation<K, V> {
    @Override
    public CompletableFuture<V> compute(K k) {
        return CompletableFuture.supplyAsync(() -> computeInternal(k));
    }
    
    public abstract V computeInternal(K k);
}
