package com.ran.interaction.controllers;

import com.ran.filesystem.descriptor.ProblemDescriptor;
import com.ran.filesystem.supplier.FileSupplier;
import com.ran.interaction.panels.GeneralPanel;
import com.ran.interaction.panels.TestsPanel;
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
        dialog.getGeneralPanel().setProblemName(descriptor.getProblemName());
        dialog.getGeneralPanel().setTimeLimit(descriptor.getTimeLimit());
        dialog.getGeneralPanel().setMemoryLimit(descriptor.getMemoryLimit());
        dialog.getTestsPanel().subscribe(TestsPanel.ADD, this::addTest);
        dialog.getTestsPanel().subscribe(TestsPanel.VIEW_INPUT, this::viewInputTest);
        dialog.getTestsPanel().subscribe(TestsPanel.VIEW_ANSWER, this::viewAnswerTest);
        dialog.getTestsPanel().subscribe(TestsPanel.DELETE, this::deleteTest);
        dialog.getTestsPanel().subscribe(TestsPanel.UPDATE, this::updateTests);
        dialog.getTestsPanel().subscribe(TestsPanel.SAVE_POINTS, this::savePointsForTestGroup);
        dialog.getTestsPanel().subscribe(TestsPanel.CHANGE_TEST_GROUP, this::changeTestGroup);
    }
    
    // ------------------------------------------------------------
    // Listeners for GeneralPanel
    // ------------------------------------------------------------    
    
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
    
    // ------------------------------------------------------------
    // Listeners for TestsPanel
    // ------------------------------------------------------------  
    
    private void addTest(String id, Object parameter) {
        System.out.println("Add test");
    }
    
    private void viewInputTest(String id, Object parameter) {
        System.out.println("View input test");
    }
    
    private void viewAnswerTest(String id, Object parameter) {
        System.out.println("View answer test");
    }
    
    private void deleteTest(String id, Object parameter) {
        System.out.println("Delete test");
    }
    
    private void updateTests(String id, Object parameter) {
        System.out.println("Update tests");
    }
    
    private void savePointsForTestGroup(String id, Object parameter) {
        System.out.println("Save points");
    }
    
    private void changeTestGroup(String id, Object parameter) {
        System.out.println("Change test group");
    }
    
    // ------------------------------------------------------------
    // Listeners for GeneratorsPanel
    // ------------------------------------------------------------  
    
    // ------------------------------------------------------------
    // Listeners for ValidatorsPanel
    // ------------------------------------------------------------  
    
    // ------------------------------------------------------------
    // Listeners for CheckersPanel
    // ------------------------------------------------------------  
    
    // ------------------------------------------------------------
    // Listeners for AuthorDecisionsPanel
    // ------------------------------------------------------------  
    
}