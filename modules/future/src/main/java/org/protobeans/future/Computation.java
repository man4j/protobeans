package org.protobeans.future;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface Computation<K, V> {
    CompletableFuture<V> compute(K k, Function<K, V> f);
}
