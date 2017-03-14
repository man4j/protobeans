package org.protobeans.future;

import java.util.concurrent.CompletableFuture;

public interface Computation<K, V> {
    CompletableFuture<V> compute(K k);
}
