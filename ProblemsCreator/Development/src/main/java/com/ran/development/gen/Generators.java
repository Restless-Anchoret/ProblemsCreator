package com.ran.development.gen;

import com.ran.development.logging.DevelopmentLogging;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.function.Supplier;
import java.util.logging.Level;

public class Generators {

    private Generators() { }
    
    public static Supplier<? extends Generator> getGeneratorSupplier(Path folderPath, String className) {
        try {
            ClassLoader loader = new URLClassLoader(new URL[] { folderPath.toUri().toURL() });
            Class<? extends Generator> cl = (Class<? extends Generator>)loader.loadClass(className);
            cl.newInstance();
            return () -> {
                try {
                    return cl.newInstance();
                } catch (InstantiationException | IllegalAccessException exception) {
                    DevelopmentLogging.logger.log(Level.FINE, "Cannot create instance of Generator subclass", exception);
                    return null;
                }
            };
        } catch (Throwable throwable) {
            DevelopmentLogging.logger.log(Level.FINE, "Cannot load Generator subclass", throwable);
            return null;
        }
    }
    
}