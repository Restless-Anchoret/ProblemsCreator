package com.ran.testing.registry;

import java.util.Collection;
import java.util.function.Supplier;

public interface Registry<T> {

    T getDefault();
    void put(String id, Supplier<T> supplier);
    T get(String id);
    Collection<String> getAvailableIds();
    
}
