package com.ran.interaction.controllers;

import com.ran.filesystem.descriptor.ProblemDescriptor;
import com.ran.filesystem.supplier.FileSupplier;
import com.ran.interaction.panels.GeneralPanel;
import com.ran.interaction.panels.ProblemsPanel;
import com.ran.interaction.support.SwingUtil;
import com.ran.interaction.windows.ProblemDialog;

public class ProblemController {

    private ProblemDialog dialog = null;
    private FileSupplier fileSupplier = null;
    private String problemFolder = null;

    public void setFileSupplier(FileSupplier fileSupplier) {
        this.fileSupplier = fileSupplier;
    }
    
    public void setProblemFolder(String problemFolder) {
        this.problemFolder = problemFolder;
    }
    
    public void showDialog() {
        dialog = new ProblemDialog(null, true);
        configureDialog(dialog);
        dialog.setVisible(true);
    }
    
    public void configureDialog(ProblemDialog dialog) {
        ProblemDescriptor descriptor = fileSupplier.getProblemDescriptor(problemFolder);
        dialog.getGeneralPanel().subscribe(GeneralPanel.SAVE, this::saveGeneralProblemChanges);
        dialog.getGeneralPanel().subscribe(GeneralPanel.EXPORT, this::exportProblem);
        dialog.getGeneralPanel().setProblemName(descriptor.getProblemName());
        dialog.getGeneralPanel().setTimeLimit(descriptor.getTimeLimit());
        dialog.getGeneralPanel().setMemoryLimit(descriptor.getMemoryLimit());
    }
    
    private void saveGeneralProblemChanges(String id, Object parameter) {
        GeneralPanel panel = dialog.getGeneralPanel();
        String problemName = panel.getProblemName();
        Integer timeLimit = panel.getTimeLimit();
        Integer memoryLimit = panel.getMemoryLimit();
        if (problemName.isEmpty() || timeLimit == null || memoryLimit == null) {
            SwingUtil.showMessageDialog(dialog, "Not correct values", "Not correct values");
            return;
        }
        ProblemDescriptor descriptor = fileSupplier.getProblemDescriptor(problemFolder);
        descriptor.setProblemName(problemName);
        descriptor.setTimeLimit(timeLimit);
        descriptor.setMemoryLimit(memoryLimit);
        descriptor.persist();
    }
    
    private void exportProblem(String id, Object parameter) {
        System.out.println("Unsupported operation");
    }
    
}