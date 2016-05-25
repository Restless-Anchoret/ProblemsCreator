package com.ran.testing.language;

import com.ran.testing.registry.AbstractRegistry;
import com.ran.testing.registry.Suppliers;

public class LanguageToolkitRegistry extends AbstractRegistry<LanguageToolkit> {

    public static final String JAVA_ID = "java";
    public static final String VISUAL_CPP_ID = "visual_cpp";
    
    private static final LanguageToolkitRegistry registry = new LanguageToolkitRegistry();
    
    static {
        registry.put(JAVA_ID, Suppliers.createSingletonSupplier(JavaLanguageToolkit.class));
        registry.put(VISUAL_CPP_ID, Suppliers.createSingletonSupplier(VisualCppLanguageToolkit.class));
    }
    
    public static LanguageToolkitRegistry registry() {
        return registry;
    }
    
    private LanguageToolkitRegistry() { }

    @Override
    public LanguageToolkit getDefault() {
        return get(JAVA_ID);
    }
    
}