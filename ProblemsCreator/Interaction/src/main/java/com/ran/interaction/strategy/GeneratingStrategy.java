package com.ran.interaction.strategy;

public class GeneratingStrategy extends AbstractDevelopmentStrategy {

    @Override
    protected void performDevelopmentProcess() {
        System.out.println("Perform generating process");
    }

    @Override
    public void saveDevelopmentResults() {
        System.out.println("Save generating results");
    }

}