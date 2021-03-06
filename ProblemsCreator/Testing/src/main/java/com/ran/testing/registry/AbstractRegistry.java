package com.ran.testing.registry;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class AbstractRegistry<T> implements Registry<T> {

    private Map<String, Supplier<T>> map = new HashMap<>();
    
    @Override
    public void put(String id, Supplier<T> supplier) {
        map.put(id, supplier);
    }

    @Override
    public T get(String id) {
        return map.get(id).get();
    }

    @Override
    public Collection<String> getAvailableIds() {
        return Collections.unmodifiableCollection(map.keySet());
    }

}