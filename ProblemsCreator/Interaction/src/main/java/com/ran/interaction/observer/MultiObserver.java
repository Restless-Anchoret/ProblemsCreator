package com.ran.interaction.observer;

import java.util.HashSet;
import java.util.Set;

public class MultiObserver implements Observer {

    private Set<Observer> observers = new HashSet<>();
    
    public Observer addObserver(Observer observer) {
        observers.add(observer);
        return this;
    }
    
    public Observer removeObserver(Observer observer) {
        observers.remove(observer);
        return this;
    }

    public Set<Observer> getObservers() {
        return observers;
    }
    
    @Override
    public void notify(String id, Object parameter) {
        for (Observer observer: observers) {
            observer.notify(id, parameter);
        }
    }

}