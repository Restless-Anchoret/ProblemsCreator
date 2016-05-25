package com.ran.testing.registry;

import com.ran.testing.logging.TestingLogging;
import java.util.function.Supplier;
import java.util.logging.Level;

public class Suppliers {

    private Suppliers() { }
    
    public static <T> Supplier<T> createNewInstanceSupplier(Class<? extends T> cl) {
        return new Supplier<T>() {
            @Override
            public T get() {
                try {
                    return cl.newInstance();
                } catch (IllegalAccessException | InstantiationException exception) {
                    String message = "Exception while instantiating " + cl.getName() + " object";
                    TestingLogging.logger.log(Level.FINE, message, exception);
                    return null;
                }
            }
        };
    }
    
    public static <T> Supplier<T> createSingletonSupplier(Class<? extends T> cl) {
        try {
            final T instance = cl.newInstance();
            return new Supplier<T>() {
                @Override
                public T get() {
                    return instance;
                }
            };
        } catch (IllegalAccessException | InstantiationException exception) {
            String message = "Exception while instantiating " + cl.getName() + " object";
            TestingLogging.logger.log(Level.FINE, message, exception);
            return null;
        }
    }
    
}