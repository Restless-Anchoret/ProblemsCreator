package com.ran.development.util;

import com.ran.development.logging.DevelopmentLogging;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.function.Supplier;
import java.util.logging.Level;

public class Utils {

    private Utils() { }
    
    public static <T> Supplier<? extends T> getSupplier(Class<T> parentClass,
            Path classFilePath) {
        try {
            Path folderPath = classFilePath.getParent();
            String className = getClassName(classFilePath);
            ClassLoader loader = new URLClassLoader(new URL[] { folderPath.toUri().toURL() });
            Class<? extends T> cl = (Class<? extends T>)loader.loadClass(className);
            cl.newInstance();
            return () -> {
                try {
                    return cl.newInstance();
                } catch (InstantiationException | IllegalAccessException exception) {
                    String message = "Cannot create instance of " + parentClass.getName() +
                            " subclass";
                    DevelopmentLogging.logger.log(Level.FINE, message, exception);
                    return null;
                }
            };
        } catch (Throwable throwable) {
            String message = "Cannot load " + parentClass.getName() + " subclass";
            DevelopmentLogging.logger.log(Level.FINE, message, throwable);
            return null;
        }
    }
    
    private static String getClassName(Path classFilePath) {
        String fileName = classFilePath.getFileName().toString();
        int index = fileName.lastIndexOf('.');
        return fileName.substring(0, index);
    }
    
}