package org.protobeans.future;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class ForkJoinPooledComputation<K, V> implements Computation<K, V> {
    @Override
    public CompletableFuture<V> compute(K k, Function<K, V> f) {
        return CompletableFuture.supplyAsync(() -> f.apply(k));
    }
}
