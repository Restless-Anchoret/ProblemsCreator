package com.ran.interaction.controllers;

import com.ran.interaction.windows.ProblemDialog;

public class ProblemController {

    private ProblemDialog dialog = null;
    private String problemFolder = null;

    public void setProblemFolder(String problemFolder) {
        this.problemFolder = problemFolder;
    }
    
    public void showDialog() {
        dialog = new ProblemDialog(null, true);
        configureDialog(dialog);
        dialog.setVisible(true);
    }
    
    public void configureDialog(ProblemDialog dialog) {
        
    }
    
}