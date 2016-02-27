package com.ran.testing.registry;

public interface Registry<T> {

    T getDefault();
    void put(String id, Supplier<T> supplier);
    T get(String id);
    
}
