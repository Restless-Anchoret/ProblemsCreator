package com.ran.interaction.observer;

@FunctionalInterface
public interface Observer {

    void notify(String id, Object parameter);
    
}
