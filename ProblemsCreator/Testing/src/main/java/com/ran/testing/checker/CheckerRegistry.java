package com.ran.testing.checker;

import com.ran.testing.registry.AbstractRegistry;
import com.ran.testing.registry.Suppliers;

public class CheckerRegistry extends AbstractRegistry<Checker> {

    private static final String matchId = "match";
    private static final String defaultId = "default";
    
    private static final CheckerRegistry registry = new CheckerRegistry();
    
    static {
        registry.put(matchId, Suppliers.createSingletonSupplier(MatchChecker.class));
        registry.put(defaultId, Suppliers.createSingletonSupplier(MatchChecker.class));
    }
    
    public static CheckerRegistry registry() {
        return registry;
    }
    
    private CheckerRegistry() { }

    @Override
    public Checker getDefault() {
        return get(defaultId);
    }
    
}