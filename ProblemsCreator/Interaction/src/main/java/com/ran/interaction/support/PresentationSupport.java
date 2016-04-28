package com.ran.interaction.support;

import com.ran.interaction.logging.InteractionLogging;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;

public class PresentationSupport {

    private static Properties properties = null;
    
    public static Properties getPresentationProperties() {
        if (properties == null) {
            properties = new Properties();
            try (InputStream propertiesStream = PresentationSupport.class.getResourceAsStream("presentation.properties")) {
                properties.load(propertiesStream);
            } catch (Exception exception) {
                InteractionLogging.logger.log(Level.FINE, "Exception while loading presentation.properties", exception);
            }
        }
        return properties;
    }
    
    private PresentationSupport() { }
    
}