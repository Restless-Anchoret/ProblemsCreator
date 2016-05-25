package com.ran.interaction.controllers;

import com.ran.interaction.strategy.DevelopmentStrategy;
import com.ran.interaction.windows.DevelopmentDialog;

public class DevelopmentController {

    private DevelopmentStrategy developmentStrategy = null;
    private DevelopmentDialog dialog = null;

    public void setDevelopmentStrategy(DevelopmentStrategy developmentStrategy) {
        this.developmentStrategy = developmentStrategy;
    }
    
    public void showDialog() {
        dialog = new DevelopmentDialog(null, true);
        configureDialog(dialog);
        developmentStrategy.setOutputConsumer(line -> {
            dialog.getTextAreaOutput().append(line + "\n");
        });
        developmentStrategy.runDevelopmentProcess();
        dialog.setVisible(true);
    }
    
    private void configureDialog(DevelopmentDialog dialog) {
        dialog.subscribe(DevelopmentDialog.SAVE, this::saveDevelopmentResults);
        dialog.subscribe(DevelopmentDialog.INTERRUPT, this::interruptDevelopmentProcess);
        dialog.subscribe(DevelopmentDialog.CLOSE, this::closeDialog);
        dialog.setSaveAbility(developmentStrategy.isNeedSaveAbility());
        dialog.setExecutingFileName(developmentStrategy.getExecutingFileName());
    }
    
    private void saveDevelopmentResults(String id, Object parameter) {
        developmentStrategy.saveDevelopmentResults();
    }
    
    private void interruptDevelopmentProcess(String id, Object parameter) {
        developmentStrategy.interruptDevelopmentProcess();
    }
    
    private void closeDialog(String id, Object parameter) {
        developmentStrategy.interruptDevelopmentProcess();
        dialog.setVisible(false);
    }
    
}