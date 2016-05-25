package com.ran.interaction.strategy;

public class CreatingAnswersStrategy extends AbstractDevelopmentStrategy {

    @Override
    protected void performDevelopmentProcess() {
        System.out.println("Perform creating answers process");
    }

    @Override
    public void saveDevelopmentResults() {
        System.out.println("Save answers");
    }

}