package com.ran.interaction.observer;

import java.util.List;

public interface Publisher {

    List<String> getAvailableIds();
    void subscribe(String id, Observer observer);
    Observer getObserver(String id);
    
    default void initObservers() {
        for (String id: getAvailableIds()) {
            subscribe(id, EmptyObserver.getInstanse());
        }
    }
    
    default void notifyObservers(Object parameter) {
        for (String id: getAvailableIds()) {
            getObserver(id).notify(id, parameter);
        }
    }
    
}
