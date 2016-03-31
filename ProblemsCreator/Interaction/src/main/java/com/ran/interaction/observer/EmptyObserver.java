package com.ran.interaction.observer;

public class EmptyObserver implements Observer {

    private static EmptyObserver instanse = new EmptyObserver();
    
    public static EmptyObserver getInstanse() {
        return instanse;
    }
    
    private EmptyObserver() { }
    
    @Override
    public void notify(String id, Object parameter) { }

}