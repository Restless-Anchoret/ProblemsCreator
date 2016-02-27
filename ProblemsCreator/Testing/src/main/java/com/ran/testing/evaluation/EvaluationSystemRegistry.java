package com.ran.testing.evaluation;

import com.ran.testing.registry.AbstractRegistry;
import com.ran.testing.registry.Suppliers;

public class EvaluationSystemRegistry extends AbstractRegistry<EvaluationSystem> {

    private static final String icpcId = "icpc";
    private static final String ioiId = "ioi";
    private static final String checkId = "check";
    
    private static final EvaluationSystemRegistry registry = new EvaluationSystemRegistry();
    
    static {
        registry.put(icpcId, Suppliers.createSingletonSupplier(ICPCEvaluationSystem.class));
        registry.put(ioiId, Suppliers.createSingletonSupplier(IOIEvaluationSystem.class));
        registry.put(checkId, Suppliers.createSingletonSupplier(CheckEvaluationSystem.class));
    }
    
    public static EvaluationSystemRegistry registry() {
        return registry;
    }
    
    private EvaluationSystemRegistry() { }

    @Override
    public EvaluationSystem getDefault() {
        return get(icpcId);
    }
    
}