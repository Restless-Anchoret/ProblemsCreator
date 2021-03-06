package com.ran.interaction.controllers;

import com.ran.development.gen.MultiGenerator;
import com.ran.development.valid.MultiValidator;
import com.ran.filesystem.descriptor.AuthorDecisionDescriptor;
import com.ran.filesystem.descriptor.ProblemDescriptor;
import com.ran.filesystem.descriptor.TestGroupDescriptor;
import com.ran.filesystem.supplier.CodeSupplier;
import com.ran.filesystem.supplier.FileSupplier;
import com.ran.filesystem.supplier.FilesUtil;
import com.ran.interaction.components.SelectItem;
import com.ran.interaction.logging.InteractionLogging;
import com.ran.interaction.panels.AuthorDecisionsPanel;
import com.ran.interaction.panels.CheckersPanel;
import com.ran.interaction.panels.GeneralPanel;
import com.ran.interaction.panels.GeneratorsPanel;
import com.ran.interaction.panels.TestsPanel;
import com.ran.interaction.panels.ValidatorsPanel;
import com.ran.interaction.strategy.AnswersCreator;
import com.ran.interaction.strategy.CreatingAnswersStrategy;
import com.ran.interaction.strategy.DevelopmentStrategy;
import com.ran.interaction.strategy.GeneratingStrategy;
import com.ran.interaction.strategy.ValidatingStrategy;
import com.ran.interaction.support.PresentationSupport;
import com.ran.interaction.support.SwingUtil;
import com.ran.interaction.windows.ProblemDialog;
import com.ran.testing.checker.CheckerRegistry;
import com.ran.testing.language.FailException;
import com.ran.testing.language.LanguageToolkit;
import com.ran.testing.language.LanguageToolkitRegistry;
import com.ran.testing.system.TestGroupType;
import com.ran.testing.system.TestingSystem;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import javax.swing.JOptionPane;

public class ProblemController {
    
    private static final String ALL_TESTS_SIGN = "ALL_TESTS";
    
    private ProblemDialog dialog = null;
    private FileSupplier fileSupplier = null;
    private TestingSystem testingSystem = null;
    private String problemFolder = null;
    private Properties presentationProperties = PresentationSupport.getPresentationProperties();

    public void setFileSupplier(FileSupplier fileSupplier) {
        this.fileSupplier = fileSupplier;
    }

    public void setTestingSystem(TestingSystem testingSystem) {
        this.testingSystem = testingSystem;
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
        dialog.getGeneralPanel().subscribe(GeneralPanel.LOAD_STATEMENT, this::loadStatementFile);
        dialog.getGeneralPanel().setProblemName(descriptor.getProblemName());
        dialog.getGeneralPanel().setTimeLimit(descriptor.getTimeLimit());
        dialog.getGeneralPanel().setMemoryLimit(descriptor.getMemoryLimit());
        dialog.getGeneralPanel().setStatementFileDescription(FilesUtil.getFileDescription(
                fileSupplier.getProblemStatementPath(problemFolder)));
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
        dialog.getValidatorsPanel().subscribe(ValidatorsPanel.ADD, this::addValidator);
        dialog.getValidatorsPanel().subscribe(ValidatorsPanel.VIEW_CODE, this::viewValidatorCode);
        dialog.getValidatorsPanel().subscribe(ValidatorsPanel.DELETE, this::deleteValidator);
        dialog.getValidatorsPanel().subscribe(ValidatorsPanel.UPDATE, this::updateValidators);
        dialog.getValidatorsPanel().subscribe(ValidatorsPanel.VALIDATE_TESTS, this::runValidator);
        testGroupItems.add(0, new SelectItem(ALL_TESTS_SIGN, presentationProperties.getProperty(ALL_TESTS_SIGN)));
        dialog.getValidatorsPanel().setTestGroupItems(testGroupItems);
        updateValidators(null, null);
        dialog.getCheckersPanel().subscribe(CheckersPanel.CHANGE_CHECKER, this::changeCheckerType);
        dialog.getCheckersPanel().subscribe(CheckersPanel.VIEW_CODE, this::viewCheckerCode);
        dialog.getCheckersPanel().subscribe(CheckersPanel.RECOMPILE, this::recompileCheckerCode);
        dialog.getCheckersPanel().setCheckerTypeItems(getCheckerTypeSelectItems());
        dialog.getCheckersPanel().setSelectedCheckerType(fileSupplier
                .getProblemDescriptor(problemFolder).getCheckerType());
        dialog.getAuthorDecisionsPanel().subscribe(AuthorDecisionsPanel.ADD, this::addAuthorDecision);
        dialog.getAuthorDecisionsPanel().subscribe(AuthorDecisionsPanel.VIEW_CODE, this::viewAuthorDecisionCode);
        dialog.getAuthorDecisionsPanel().subscribe(AuthorDecisionsPanel.RUN, this::runAuthorDecision);
        dialog.getAuthorDecisionsPanel().subscribe(AuthorDecisionsPanel.DELETE, this::deleteAuthorDecision);
        dialog.getAuthorDecisionsPanel().subscribe(AuthorDecisionsPanel.UPDATE, this::updateAuthorDecisions);
        dialog.getAuthorDecisionsPanel().subscribe(AuthorDecisionsPanel.CREATE_ANSWERS, this::createAnswersForAuthorDecision);
        dialog.getAuthorDecisionsPanel().setTestGroupItems(testGroupItems);
        updateAuthorDecisions(null, null);
    }
    
    private List<SelectItem> getTestGroupSelectItems() {
        List<SelectItem> testGroupItems = new ArrayList<>();
        for (TestGroupType type: TestGroupType.values()) {
            testGroupItems.add(new SelectItem(type.toString().toLowerCase(),
                    presentationProperties.getProperty(type.toString())));
        }
        return testGroupItems;
    }
    
    private List<SelectItem> getCheckerTypeSelectItems() {
        List<SelectItem> checkerTypeItems = new ArrayList<>();
        for (String checkerTypeId: CheckerRegistry.registry().getAvailableIds()) {
            checkerTypeItems.add(new SelectItem(checkerTypeId, presentationProperties.getProperty(checkerTypeId)));
        }
        return checkerTypeItems;
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
    
    private void loadStatementFile(String id, Object parameter) {
        if (dialog.getGeneralPanel().getNewStatementPath() == null) {
            SwingUtil.showMessageDialog(dialog, GeneralPanel.FILE_PATH_MESSAGE,
                    GeneralPanel.FILE_PATH_TITLE);
            return;
        }
        boolean puttingStatementSuccess = fileSupplier.putProblemStatementPath(problemFolder,
                dialog.getGeneralPanel().getNewStatementPath());
        if (!puttingStatementSuccess) {
            SwingUtil.showErrorDialog(dialog, GeneralPanel.FILE_LOADING_ERROR_MESSAGE,
                    GeneralPanel.FILE_LOADING_ERROR_TITLE);
        }
        dialog.getGeneralPanel().setStatementFileDescription(FilesUtil.getFileDescription(
                fileSupplier.getProblemStatementPath(problemFolder)));
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
        fileSupplier.addGeneratorFolder(problemFolder);
        updateGenerators(null, null);
    }
    
    public void viewGeneratorCode(String id, Object parameter) {
        String generatorFolder = parameter.toString();
        Path generatorPath = fileSupplier.getGeneratorCodeSupplier(
                problemFolder, generatorFolder).getSourceFile();
        FileEditorController controller = new FileEditorController();
        controller.showDialog(generatorPath, false);
        updateGenerators(null, null);
    }
    
    public void deleteGenerator(String id, Object parameter) {
        int answer = SwingUtil.showYesNoDialog(dialog, GeneratorsPanel.DELETING_GENERATOR_MESSAGE,
                GeneratorsPanel.DELETING_GENERATOR_TITLE);
        if (answer == JOptionPane.YES_OPTION) {
            String generatorFolder = parameter.toString();
            fileSupplier.deleteGeneratorFolder(problemFolder, generatorFolder);
            updateGenerators(null, null);
        }
    }
    
    public void updateGenerators(String id, Object parameter) {
        List<String> generatorFolderNames = fileSupplier.getGeneratorFolders(problemFolder);
        Object[][] content = SwingUtil.prepareTableContent(generatorFolderNames, (number, row) -> {
            row.add(number);
            Path generatorPath = fileSupplier.getGeneratorCodeSupplier(problemFolder, number).getSourceFile();
            row.add(FilesUtil.getFileDescription(generatorPath));
        });
        dialog.getGeneratorsPanel().setTableContent(content);
    }
    
    public void runGenerator(String id, Object parameter) {
        Integer randomSeed = dialog.getGeneratorsPanel().getRandomSeed();
        Integer testsQuantity = dialog.getGeneratorsPanel().getTestsToGenerateQuantity();
        if (randomSeed == null) {
            SwingUtil.showErrorDialog(dialog, GeneratorsPanel.INCORRECT_RANDOM_SEED_MESSAGE,
                    GeneratorsPanel.INCORRECT_RANDOM_SEED_TITLE);
            return;
        }
        if (testsQuantity == null || testsQuantity <= 0) {
            SwingUtil.showErrorDialog(dialog, GeneratorsPanel.INCORRECT_TESTS_QUANTITY_MESSAGE,
                    GeneratorsPanel.INCORRECT_TESTS_QUANTITY_TITLE);
            return;
        }
        String generatorFolder = parameter.toString();
        String testGroupType = dialog.getGeneratorsPanel().getTestGroupType();
        Path[] paths = getTempFiles(testsQuantity);
        DevelopmentController controller = new DevelopmentController();
        MultiGenerator multiGenerator = new MultiGenerator();
        multiGenerator.setPaths(paths);
        multiGenerator.setArguments(dialog.getGeneratorsPanel().getArguments());
        multiGenerator.setRandomSeed(randomSeed);
        GeneratingStrategy strategy = new GeneratingStrategy(multiGenerator);
        strategy.setProblemFolder(problemFolder);
        strategy.setTestGroupToGenerate(testGroupType);
        strategy.setFileSupplier(fileSupplier);
        strategy.setCodeSupplier(fileSupplier.getGeneratorCodeSupplier(problemFolder, generatorFolder));
        controller.setDevelopmentStrategy(strategy);
        controller.showDialog();
        deleteTempFiles(paths);
        updateTests(null, null);
    }
    
    // ------------------------------------------------------------
    // Listeners for ValidatorsPanel
    // ------------------------------------------------------------  
    
    public void addValidator(String id, Object parameter) {
        fileSupplier.addValidatorFolder(problemFolder);
        updateValidators(null, null);
    }
    
    public void viewValidatorCode(String id, Object parameter) {
        String validatorFolder = parameter.toString();
        Path validatorPath = fileSupplier.getValidatorCodeSupplier(
                problemFolder, validatorFolder).getSourceFile();
        FileEditorController controller = new FileEditorController();
        controller.showDialog(validatorPath, false);
        updateValidators(null, null);
    }
    
    public void deleteValidator(String id, Object parameter) {
        int answer = SwingUtil.showYesNoDialog(dialog, ValidatorsPanel.DELETING_VALIDATOR_MESSAGE,
                ValidatorsPanel.DELETING_VALIDATOR_TITLE);
        if (answer == JOptionPane.YES_OPTION) {
            String validatorFolder = parameter.toString();
            fileSupplier.deleteValidatorFolder(problemFolder, validatorFolder);
            updateValidators(null, null);
        }
    }
    
    public void updateValidators(String id, Object parameter) {
        List<String> validatorFolderNames = fileSupplier.getValidatorFolders(problemFolder);
        Object[][] content = SwingUtil.prepareTableContent(validatorFolderNames, (number, row) -> {
            row.add(number);
            Path validatorPath = fileSupplier.getValidatorCodeSupplier(problemFolder, number).getSourceFile();
            row.add(FilesUtil.getFileDescription(validatorPath));
        });
        dialog.getValidatorsPanel().setTableContent(content);
    }
    
    public void runValidator(String id, Object parameter) {
        String validatorFolder = parameter.toString();
        Path[] inputPaths = getInputTestPaths(dialog.getValidatorsPanel().getTestGroupType()).toArray(new Path[] { });
        DevelopmentController controller = new DevelopmentController();
        MultiValidator multiValidator = new MultiValidator();
        multiValidator.setArguments(dialog.getValidatorsPanel().getArguments());
        multiValidator.setPaths(inputPaths);
        DevelopmentStrategy strategy = new ValidatingStrategy(multiValidator);
        strategy.setFileSupplier(fileSupplier);
        strategy.setCodeSupplier(fileSupplier.getValidatorCodeSupplier(problemFolder, validatorFolder));
        controller.setDevelopmentStrategy(strategy);
        controller.showDialog();
    }
    
    // ------------------------------------------------------------
    // Listeners for CheckersPanel
    // ------------------------------------------------------------  
    
    public void changeCheckerType(String id, Object parameter) {
        String selectedCheckerType = dialog.getCheckersPanel().getSelectedCheckerType();
        ProblemDescriptor descriptor = fileSupplier.getProblemDescriptor(problemFolder);
        descriptor.setCheckerType(selectedCheckerType);
        descriptor.persist();
    }
    
    public void viewCheckerCode(String id, Object parameter) {
        Path checkerSourcePath = fileSupplier.getCheckerCodeSupplier(problemFolder).getSourceFile();
        FileEditorController controller = new FileEditorController();
        controller.showDialog(checkerSourcePath, false);
        recompileCheckerCode(null, null);
    }
    
    public void recompileCheckerCode(String id, Object parameter) {
        LanguageToolkit languageToolkit = LanguageToolkitRegistry.registry().getDefault();
        CodeSupplier supplier = fileSupplier.getCheckerCodeSupplier(problemFolder);
        int compilationResult = 1;
        try {
            compilationResult = languageToolkit.compile(supplier.getSourceFile(),
                    supplier.getCompileFolder(), fileSupplier.getConfigurationFolder());
        } catch (FailException exception) {
            InteractionLogging.logger.log(Level.FINE, "FailException while checker's compilation", exception);
        }
        if (compilationResult == 0) {
            SwingUtil.showMessageDialog(dialog, CheckersPanel.CHECKER_COMPILATION_SUCCESS,
                    CheckersPanel.CHECKER_COMPILATION_TITLE);
        } else {
            SwingUtil.showMessageDialog(dialog, CheckersPanel.CHECKER_COMPILATION_FAIL,
                    CheckersPanel.CHECKER_COMPILATION_TITLE);
        }
    }
    
    // ------------------------------------------------------------
    // Listeners for AuthorDecisionsPanel
    // ------------------------------------------------------------
    
    public void addAuthorDecision(String id, Object parameter) {
        AddingAuthorDecisionController controller = new AddingAuthorDecisionController();
        controller.setFileSupplier(fileSupplier);
        controller.setProblemFolder(problemFolder);
        controller.showDialog();
        updateAuthorDecisions(null, null);
    }
    
    public void viewAuthorDecisionCode(String id, Object parameter) {
        String authorDecisionFolder = parameter.toString();
        Path authorDecisionSourcePath = fileSupplier.getAuthorDecisionCodeSupplier(
                problemFolder, authorDecisionFolder).getSourceFile();
        FileEditorController controller = new FileEditorController();
        controller.showDialog(authorDecisionSourcePath, true);
        updateAuthorDecisions(null, null);
    }
    
    public void runAuthorDecision(String id, Object parameter) {
        String authorDecisionFolder = parameter.toString();
        SubmissionResultController resultController = new SubmissionResultController();
        resultController.setFileSupplier(fileSupplier);
        resultController.setTestingSystem(testingSystem);
        resultController.setAuthorDecisionFolder(authorDecisionFolder);
        resultController.setProblemFolder(problemFolder);
        resultController.showDialog();
    }
    
    public void deleteAuthorDecision(String id, Object parameter) {
        int answer = SwingUtil.showYesNoDialog(dialog, AuthorDecisionsPanel.DELETING_MESSAGE,
                AuthorDecisionsPanel.DELETING_TITLE);
        if (answer == JOptionPane.YES_OPTION) {
            String authorDecisionFolder = parameter.toString();
            fileSupplier.deleteAuthorDecisionFolder(problemFolder, authorDecisionFolder);
            updateAuthorDecisions(null, null);
        }
    }
    
    public void updateAuthorDecisions(String id, Object parameter) {
        List<String> authorDecisionFolders = fileSupplier.getAuthorDecisionsFolderNames(problemFolder);
        Object[][] content = SwingUtil.prepareTableContent(authorDecisionFolders, (number, row) -> {
            row.add(number);
            Path authorDecisionSourcePath = fileSupplier.getAuthorDecisionCodeSupplier(
                    problemFolder, number).getSourceFile();
            row.add(FilesUtil.getFileDescription(authorDecisionSourcePath));
            AuthorDecisionDescriptor descriptor = fileSupplier.getAuthorDecisionDescriptor(problemFolder, number);
            row.add(descriptor.getTitle());
            row.add(presentationProperties.getProperty(descriptor.getCompilatorName()));
        });
        dialog.getAuthorDecisionsPanel().setTableContent(content);
    }
    
    public void createAnswersForAuthorDecision(String id, Object parameter) {
        String authorDecisionFolder = parameter.toString();
        String testGroupType = dialog.getAuthorDecisionsPanel().getSelectedTestGroupType();
        AuthorDecisionDescriptor descriptor = fileSupplier.getAuthorDecisionDescriptor(problemFolder, authorDecisionFolder);
        LanguageToolkit languageToolkit = LanguageToolkitRegistry.registry().get(descriptor.getCompilatorName());
        AnswersCreator answersCreator = new AnswersCreator();
        answersCreator.setPathInputs(getInputTestPaths(testGroupType));
        List<Path> tempAnswerPaths = getTempAnswerTestPaths(testGroupType);
        answersCreator.setPathAnswers(tempAnswerPaths);
        CreatingAnswersStrategy strategy = new CreatingAnswersStrategy(answersCreator);
        strategy.setFileSupplier(fileSupplier);
        strategy.setCodeSupplier(fileSupplier.getAuthorDecisionCodeSupplier(problemFolder, authorDecisionFolder));
        strategy.setProblemFolder(problemFolder);
        strategy.setTestGroupTypes(convertTypesToList(testGroupType));
        strategy.setLanguageToolkit(languageToolkit);
        DevelopmentController controller = new DevelopmentController();
        controller.setDevelopmentStrategy(strategy);
        controller.showDialog();
        deleteTempFiles(tempAnswerPaths);
        updateTests(null, null);
    }
    
    // ------------------------------------------------------------
    // Util methods
    // ------------------------------------------------------------
    
    private List<Path> getInputTestPaths(String testGroupType) {
        List<Path> listPaths = new ArrayList<>();
        List<String> types = convertTypesToList(testGroupType);
        for (String type: types) {
            for (int testNumber = 1; testNumber <= fileSupplier
                    .getTestsQuantity(problemFolder, type); testNumber++) {
                listPaths.add(fileSupplier.getTestInputFile(problemFolder, type, testNumber));
            }
        }
        return listPaths;
    }
    
    private List<Path> getTempAnswerTestPaths(String testGroupType) {
        List<Path> listPaths = new ArrayList<>();
        List<String> types = convertTypesToList(testGroupType);
        for (String type: types) {
            for (int testNumber = 1; testNumber <= fileSupplier
                    .getTestsQuantity(problemFolder, type); testNumber++) {
                listPaths.add(fileSupplier.getTempFile());
            }
        }
        return listPaths;
    }
    
    private List<String> convertTypesToList(String testGroupType) {
        if (!ALL_TESTS_SIGN.equals(testGroupType)) {
            return Arrays.asList(testGroupType);
        }
        List<String> convertedList = new ArrayList<>();
        for (TestGroupType type: TestGroupType.values()) {
            convertedList.add(type.toString().toLowerCase());
        }
        return convertedList;
    }
    
    private Path[] getTempFiles(int testsQuantity) {
        Path[] tempFiles = new Path[testsQuantity];
        for (int i = 0; i < testsQuantity; i++) {
            tempFiles[i] = fileSupplier.getTempFile();
        }
        return tempFiles;
    }
    
    private void deleteTempFiles(Path[] paths) {
        deleteTempFiles(Arrays.asList(paths));
    }
    
    private void deleteTempFiles(List<Path> paths) {
        for (Path path: paths) {
            fileSupplier.deleteTempFile(path);
        }
    }
    
}