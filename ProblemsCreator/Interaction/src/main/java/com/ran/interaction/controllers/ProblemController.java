package com.ran.interaction.controllers;

import com.ran.filesystem.descriptor.ProblemDescriptor;
import com.ran.filesystem.descriptor.TestGroupDescriptor;
import com.ran.filesystem.supplier.FileSupplier;
import com.ran.filesystem.supplier.FilesUtil;
import com.ran.interaction.components.SelectItem;
import com.ran.interaction.panels.GeneralPanel;
import com.ran.interaction.panels.GeneratorsPanel;
import com.ran.interaction.panels.TestsPanel;
import com.ran.interaction.support.PresentationSupport;
import com.ran.interaction.support.SwingUtil;
import com.ran.interaction.windows.ProblemDialog;
import com.ran.testing.system.TestGroupType;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import javax.swing.JOptionPane;

public class ProblemController {
    
    private ProblemDialog dialog = null;
    private FileSupplier fileSupplier = null;
    private String problemFolder = null;
    private Properties presentationProperties = PresentationSupport.getPresentationProperties();

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
    
    private void configureDialog(ProblemDialog dialog) {
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
        dialog.getTestsPanel().subscribe(TestsPanel.CHANGE_TEST_GROUP, this::updateTests);
        List<SelectItem> testGroupItems = getTestGroupSelectItems();
        dialog.getTestsPanel().setTestGroupItems(testGroupItems);
        updateTests(null, null);
        dialog.getGeneratorsPanel().subscribe(GeneratorsPanel.ADD, this::addGenerator);
        dialog.getGeneratorsPanel().subscribe(GeneratorsPanel.VIEW_CODE, this::viewGeneratorCode);
        dialog.getGeneratorsPanel().subscribe(GeneratorsPanel.DELETE, this::deleteGenerator);
        dialog.getGeneratorsPanel().subscribe(GeneratorsPanel.UPDATE, this::updateGenerators);
        dialog.getGeneratorsPanel().subscribe(GeneratorsPanel.GENERATE_TESTS, this::runGenerator);
        dialog.getGeneratorsPanel().setTestGroupItems(testGroupItems);
        updateGenerators(null, null);
    }
    
    private List<SelectItem> getTestGroupSelectItems() {
        List<SelectItem> testGroupItems = new ArrayList<>();
        for (TestGroupType type: TestGroupType.values()) {
            testGroupItems.add(new SelectItem(type.toString().toLowerCase(), presentationProperties.getProperty(type.toString())));
        }
        return testGroupItems;
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
        Path emptyTempFile = fileSupplier.getTempFile();
        String selectedTestGroupType = dialog.getTestsPanel().getSelectedTestGroupType();
        fileSupplier.addTestInputFiles(problemFolder, selectedTestGroupType, Arrays.asList(emptyTempFile));
        fileSupplier.deleteTempFile(emptyTempFile);
        updateTests(null, null);
    }
    
    private void viewInputTest(String id, Object parameter) {
        int testNumber = Integer.parseInt(parameter.toString());
        String selectedTestGroupType = dialog.getTestsPanel().getSelectedTestGroupType();
        Path inputFilePath = fileSupplier.getTestInputFile(problemFolder, selectedTestGroupType, testNumber);
        FileEditorController controller = new FileEditorController();
        controller.showDialog(inputFilePath, false);
        updateTests(null, null);
    }
    
    private void viewAnswerTest(String id, Object parameter) {
        int testNumber = Integer.parseInt(parameter.toString());
        String selectedTestGroupType = dialog.getTestsPanel().getSelectedTestGroupType();
        Path answerFilePath = fileSupplier.getTestAnswerFile(problemFolder, selectedTestGroupType, testNumber);
        FileEditorController controller = new FileEditorController();
        controller.showDialog(answerFilePath, false);
        updateTests(null, null);
    }
    
    private void deleteTest(String id, Object parameter) {
        int testNumber = Integer.parseInt(parameter.toString());
        String selectedTestGroupType = dialog.getTestsPanel().getSelectedTestGroupType();
        int answer = SwingUtil.showYesNoDialog(dialog, TestsPanel.DELETING_TEST_MESSAGE, TestsPanel.DELETING_TEST_TITLE);
        if (answer == JOptionPane.YES_OPTION) {
            fileSupplier.deleteTests(problemFolder, selectedTestGroupType, Arrays.asList(testNumber));
            updateTests(null, null);
        }
    }
    
    private void updateTests(String id, Object parameter) {
        String selectedTestGroupType = dialog.getTestsPanel().getSelectedTestGroupType();
        int testsQuantity = fileSupplier.getTestsQuantity(problemFolder, selectedTestGroupType);
        List<Integer> testNumbers = new ArrayList<>(testsQuantity);
        for (int i = 1; i <= testsQuantity; i++) {
            testNumbers.add(i);
        }
        Object[][] content = SwingUtil.prepareTableContent(testNumbers, (number, row) -> {
            row.add(number);
            Path inputFile = fileSupplier.getTestInputFile(problemFolder, selectedTestGroupType, number);
            Path answerFile = fileSupplier.getTestAnswerFile(problemFolder, selectedTestGroupType, number);
            row.add(FilesUtil.getFileDescription(inputFile));
            row.add(FilesUtil.getFileDescription(answerFile));
        });
        dialog.getTestsPanel().setTableContent(content);
        TestGroupDescriptor descriptor = fileSupplier.getTestGroupDescriptor(problemFolder, selectedTestGroupType);
        dialog.getTestsPanel().setPointsForTest(descriptor.getPointsForTest());
    }
    
    private void savePointsForTestGroup(String id, Object parameter) {
        Integer newPoints = dialog.getTestsPanel().getPointsForTest();
        if (newPoints == null) {
            SwingUtil.showErrorDialog(dialog, TestsPanel.INCORRECT_POINTS_MESSAGE, TestsPanel.INCORRECT_POINTS_TITLE);
            return;
        }
        String selectedTestGroupType = dialog.getTestsPanel().getSelectedTestGroupType();
        TestGroupDescriptor descriptor = fileSupplier.getTestGroupDescriptor(problemFolder, selectedTestGroupType);
        descriptor.setPointsForTest(newPoints);
        descriptor.persist();
    }
    
    // ------------------------------------------------------------
    // Listeners for GeneratorsPanel
    // ------------------------------------------------------------  
    
    public void addGenerator(String id, Object parameter) {
        System.out.println("Add generator");
    }
    
    public void viewGeneratorCode(String id, Object parameter) {
        System.out.println("View generator code: " + parameter);
    }
    
    public void deleteGenerator(String id, Object parameter) {
        System.out.println("Delete generator: " + parameter);
    }
    
    public void updateGenerators(String id, Object parameter) {
        System.out.println("Update generators");
    }
    
    public void runGenerator(String id, Object parameter) {
        System.out.println("Run generator: " + parameter);
    }
    
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