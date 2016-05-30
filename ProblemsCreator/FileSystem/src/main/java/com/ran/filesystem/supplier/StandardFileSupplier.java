package com.ran.filesystem.supplier;

import com.ran.filesystem.descriptor.AuthorDecisionDescriptor;
import com.ran.filesystem.descriptor.ProblemDescriptor;
import com.ran.filesystem.descriptor.SubmissionDescriptor;
import com.ran.filesystem.descriptor.TestGroupDescriptor;
import com.ran.filesystem.logging.FileSystemLogging;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.logging.Level;

public class StandardFileSupplier implements FileSupplier {

    private static final String FILE_SYSTEM_FOLDER = "file_system",
                                PROBLEMS_FOLDER = "problems",
                                TESTS_FOLDER = "tests",
                                INPUT_FILE_NAME = "input.txt",
                                ANSWER_FILE_NAME = "output.txt",
                                TEST_GROUP_DESCRIPTOR_NAME = "test_group.xml",
                                CHECKER_FOLDER = "checker",
                                GENERATORS_FOLDER = "generators",
                                VALIDATORS_FOLDER = "validators",
                                AUTHOR_DECISIONS_FOLDER = "author_decisions",
                                AUTHOR_DECISION_DESCRIPTOR = "author_decision.xml",
                                PROBLEM_DESCRIPTOR_NAME = "problem.xml",
                                STATEMENT_FILE_NAME = "statement.pdf",
                                SUBMISSIONS_FOLDER = "submissions",
                                SUBMISSION_DESCRIPTOR_NAME = "submission.xml",
                                TEMP_FILES_FOLDER = "temp",
                                CONFIGURATION_FOLDER = "config",
                                GENERATOR_TEMPLATE = "GeneratorImplementation.java",
                                VALIDATOR_TEMPLATE = "ValidatorImplementation.java",
                                CHECKER_TEMPLATE = "CheckerImplementation.java";
    
    private String rootPath;

    public StandardFileSupplier(Path path) {
        this.rootPath = Paths.get(path.toAbsolutePath().toString(), FILE_SYSTEM_FOLDER).toString();
    }

    // ------------------------------------------------------------
    // Methods to work with problem folders
    // ------------------------------------------------------------
    
    @Override
    public String addProblemFolder() {
        Path problemsFolder = Paths.get(rootPath, PROBLEMS_FOLDER);
        Path newFolderPath = FilesUtil.addNewNumberSubfolder(problemsFolder);
        if (newFolderPath == null) {
            return null;
        }
        Path problemDescriptorPath = Paths.get(newFolderPath.toString(), PROBLEM_DESCRIPTOR_NAME);
        ProblemDescriptor.getEmptyProblemDescriptor(problemDescriptorPath);
        return newFolderPath.getFileName().toString();
    }
    
    @Override
    public void deleteProblemFolder(String problemFolder) {
        Path problemFolderPath = Paths.get(rootPath, PROBLEMS_FOLDER, problemFolder);
        FilesUtil.deleteRecursively(problemFolderPath);
    }

    @Override
    public List<String> getProblemsFolderNames() {
        Path problemsFolderPath = Paths.get(rootPath, PROBLEMS_FOLDER);
        return FilesUtil.getSubfolderNames(problemsFolderPath);
    }

    @Override
    public ProblemDescriptor getProblemDescriptor(String problemFolder) {
        Path descriptorPath = Paths.get(rootPath, PROBLEMS_FOLDER, problemFolder, PROBLEM_DESCRIPTOR_NAME);
        if (Files.notExists(descriptorPath)) {
            return null;
        }
        return ProblemDescriptor.getExistingProblemDescriptor(descriptorPath);
    }

    @Override
    public Path getProblemStatementPath(String problemFolder) {
        Path statementPath = Paths.get(rootPath, PROBLEMS_FOLDER, problemFolder, STATEMENT_FILE_NAME);
        if (Files.exists(statementPath)) {
            return statementPath;
        } else {
            return null;
        }
    }
    
    @Override
    public boolean putProblemStatementPath(String problemFolder, Path newStatementPath) {
        Path statementPath = Paths.get(rootPath, PROBLEMS_FOLDER, problemFolder, STATEMENT_FILE_NAME);
        try {
            Files.copy(newStatementPath, statementPath, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException exception) {
            FileSystemLogging.logger.log(Level.FINE, "IOException while putting problem statement", exception);
            return false;
        }
    }

    // ------------------------------------------------------------
    // Methods to work with tests folders
    // ------------------------------------------------------------
    
    @Override
    public boolean addTestInputFiles(String problemFolder, String testGroupType, List<Path> inputFilePaths) {
        Path testGroupFolderPath = Paths.get(rootPath, PROBLEMS_FOLDER, problemFolder,
                    TESTS_FOLDER, testGroupType);
        List<Path> paths = FilesUtil.addNewNumberSubfolders(testGroupFolderPath, inputFilePaths.size());
        if (paths == null) {
            return false;
        }
        try {
            for (int i = 0; i < inputFilePaths.size(); i++) {
                Path inputFilePath = inputFilePaths.get(i);
                Path destinationPath = paths.get(i).resolve(INPUT_FILE_NAME);
                Files.copy(inputFilePath, destinationPath);
                Path emptyAnswerPath = paths.get(i).resolve(ANSWER_FILE_NAME);
                Files.createFile(emptyAnswerPath);
            }
            return true;
        } catch (IOException exception) {
            FileSystemLogging.logger.log(Level.FINE, "IOException while copying input test files", exception);
            for (Path path: paths) {
                FilesUtil.deleteRecursively(path);
            }
            return false;
        }
    }

    @Override
    public boolean addTestAnswerFile(String problemFolder, String testGroupType, int testNumber, Path answerFilePath) {
        Path testFolderPath = Paths.get(rootPath, PROBLEMS_FOLDER, problemFolder,
                    TESTS_FOLDER, testGroupType, Integer.toString(testNumber));
        if (Files.notExists(testFolderPath)) {
            FileSystemLogging.logger.fine("Trying to save answer file for not existing test");
            return false;
        }
        Path destinationPath = Paths.get(testFolderPath.toString(), ANSWER_FILE_NAME);
        try {
            Files.copy(answerFilePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException exception) {
            FileSystemLogging.logger.log(Level.FINE, "IOException while copying answer test file", exception);
            return false;
        }
    }

    @Override
    public void deleteTests(String problemFolder, String testGroupType, List<Integer> testNumbers) {
        Path testGroupFolderPath = Paths.get(rootPath, PROBLEMS_FOLDER, problemFolder,
                    TESTS_FOLDER, testGroupType);
        for (int testNumber: testNumbers) {
            Path testPathFolder = Paths.get(testGroupFolderPath.toString(), Integer.toString(testNumber));
            FilesUtil.deleteRecursively(testPathFolder);
        }
        FilesUtil.normailizeSubfolderNames(testGroupFolderPath);
    }

    @Override
    public Path getTestInputFile(String problemFolder, String testGroupType, int testNumber) {
        Path testInputFilePath = Paths.get(rootPath, PROBLEMS_FOLDER, problemFolder,
                TESTS_FOLDER, testGroupType, Integer.toString(testNumber), INPUT_FILE_NAME);
        if (Files.exists(testInputFilePath)) {
            return testInputFilePath;
        } else {
            return null;
        }
    }

    @Override
    public Path getTestAnswerFile(String problemFolder, String testGroupType, int testNumber) {
        Path testAnswerFilePath = Paths.get(rootPath, PROBLEMS_FOLDER, problemFolder,
                TESTS_FOLDER, testGroupType, Integer.toString(testNumber), ANSWER_FILE_NAME);
        if (Files.exists(testAnswerFilePath)) {
            return testAnswerFilePath;
        } else {
            return null;
        }
    }
    
    @Override
    public int getTestsQuantity(String problemFolder, String testGroupType) {
        Path testGroupFolderPath = Paths.get(rootPath, PROBLEMS_FOLDER, problemFolder,
                TESTS_FOLDER, testGroupType);
        FilesUtil.checkFolderExists(testGroupFolderPath);
        return FilesUtil.getMaximumFolderNameNumber(testGroupFolderPath);
    }

    @Override
    public TestGroupDescriptor getTestGroupDescriptor(String problemFolder, String testGroupType) {
        Path descriptorPath = Paths.get(rootPath, PROBLEMS_FOLDER, problemFolder, TESTS_FOLDER,
                testGroupType, TEST_GROUP_DESCRIPTOR_NAME);
        FilesUtil.checkFolderExists(descriptorPath.getParent());
        if (Files.notExists(descriptorPath)) {
            return TestGroupDescriptor.getEmptyTestGroupDescriptor(descriptorPath);
        }
        return TestGroupDescriptor.getExistingTestGroupDescriptor(descriptorPath);
    }
    
    // ------------------------------------------------------------
    // Method to work with checker folders
    // ------------------------------------------------------------
    
    @Override
    public CodeSupplier getCheckerCodeSupplier(String problemFolder) {
        Path checkerFolderPath = Paths.get(rootPath, PROBLEMS_FOLDER, problemFolder,
                CHECKER_FOLDER);
        FilesUtil.checkFolderExists(checkerFolderPath);
        CodeSupplier codeSupplier = new StandardCodeSupplier(checkerFolderPath);
        if (codeSupplier.getSourceFile() == null) {
            Path templatePath = Paths.get(rootPath, CONFIGURATION_FOLDER, CHECKER_TEMPLATE);
            codeSupplier.putSourceFile(templatePath);
        }
        return codeSupplier;
    }
    
    // ------------------------------------------------------------
    // Methods to work with generator folders
    // ------------------------------------------------------------

    @Override
    public String addGeneratorFolder(String problemFolder) {
        Path generatorsFolderPath = Paths.get(rootPath, PROBLEMS_FOLDER, problemFolder, GENERATORS_FOLDER);
        Path newFolderPath = FilesUtil.addNewNumberSubfolder(generatorsFolderPath);
        if (newFolderPath == null) {
            return null;
        }
        String generatorFolder = newFolderPath.getFileName().toString();
        Path templatePath = Paths.get(rootPath, CONFIGURATION_FOLDER, GENERATOR_TEMPLATE);
        Path sourceFolderPath = getGeneratorCodeSupplier(problemFolder, generatorFolder).getSourceFolder();
        FilesUtil.copyFileToFolder(templatePath, sourceFolderPath);
        return generatorFolder;
    }

    @Override
    public void deleteGeneratorFolder(String problemFolder, String generatorFolder) {
        Path generatorFolderPath = Paths.get(rootPath, PROBLEMS_FOLDER, problemFolder, GENERATORS_FOLDER,
                generatorFolder);
        FilesUtil.deleteRecursively(generatorFolderPath);
    }

    @Override
    public List<String> getGeneratorFolders(String problemFolder) {
        Path generatorsFolderPath = Paths.get(rootPath, PROBLEMS_FOLDER, problemFolder, GENERATORS_FOLDER);
        return FilesUtil.getSubfolderNames(generatorsFolderPath);
    }

    @Override
    public CodeSupplier getGeneratorCodeSupplier(String problemFolder, String generatorFolder) {
        Path generatorFolderPath = Paths.get(rootPath, PROBLEMS_FOLDER, problemFolder, GENERATORS_FOLDER,
                generatorFolder);
        if (Files.notExists(generatorFolderPath)) {
            return null;
        }
        return new StandardCodeSupplier(generatorFolderPath);
    }
    
    // ------------------------------------------------------------
    // Methods to work with validator folders
    // ------------------------------------------------------------
    
    @Override
    public String addValidatorFolder(String problemFolder) {
        Path validatorsFolderPath = Paths.get(rootPath, PROBLEMS_FOLDER, problemFolder, VALIDATORS_FOLDER);
        Path newFolderPath = FilesUtil.addNewNumberSubfolder(validatorsFolderPath);
        if (newFolderPath == null) {
            return null;
        }
        String validatorFolder = newFolderPath.getFileName().toString();
        Path templatePath = Paths.get(rootPath, CONFIGURATION_FOLDER, VALIDATOR_TEMPLATE);
        Path sourceFolderPath = getValidatorCodeSupplier(problemFolder, validatorFolder).getSourceFolder();
        FilesUtil.copyFileToFolder(templatePath, sourceFolderPath);
        return validatorFolder;
    }

    @Override
    public void deleteValidatorFolder(String problemFolder, String validatorFolder) {
        Path validatorFolderPath = Paths.get(rootPath, PROBLEMS_FOLDER, problemFolder, VALIDATORS_FOLDER,
                validatorFolder);
        FilesUtil.deleteRecursively(validatorFolderPath);
    }

    @Override
    public List<String> getValidatorFolders(String problemFolder) {
        Path validatorsFolderPath = Paths.get(rootPath, PROBLEMS_FOLDER, problemFolder, VALIDATORS_FOLDER);
        return FilesUtil.getSubfolderNames(validatorsFolderPath);
    }

    @Override
    public CodeSupplier getValidatorCodeSupplier(String problemFolder, String validatorFolder) {
        Path validatorFolderPath = Paths.get(rootPath, PROBLEMS_FOLDER, problemFolder, VALIDATORS_FOLDER,
                validatorFolder);
        if (Files.notExists(validatorFolderPath)) {
            return null;
        }
        return new StandardCodeSupplier(validatorFolderPath);
    }
    
    // ------------------------------------------------------------
    // Methods to work with author decisions folders
    // ------------------------------------------------------------
    
    @Override
    public String addAuthorDecisionFolder(String problemFolder) {
        Path authorDecisionsFolder = Paths.get(rootPath, PROBLEMS_FOLDER, problemFolder,
                AUTHOR_DECISIONS_FOLDER);
        Path newFolderPath = FilesUtil.addNewNumberSubfolder(authorDecisionsFolder);
        if (newFolderPath == null) {
            return null;
        }
        Path authorDecisionDescriptorPath = Paths.get(newFolderPath.toString(), AUTHOR_DECISION_DESCRIPTOR);
        AuthorDecisionDescriptor.getEmptyAuthorDecisionDescriptor(authorDecisionDescriptorPath);
        return newFolderPath.getFileName().toString();
    }
    
    @Override
    public void deleteAuthorDecisionFolder(String problemFolder, String authorDecisionFolder) {
        Path authorDecisionFolderPath = Paths.get(rootPath, PROBLEMS_FOLDER, problemFolder,
                AUTHOR_DECISIONS_FOLDER, authorDecisionFolder);
        FilesUtil.deleteRecursively(authorDecisionFolderPath);
    }
    
    @Override
    public List<String> getAuthorDecisionsFolderNames(String problemFolder) {
        Path authorDecisionFolderPath = Paths.get(rootPath, PROBLEMS_FOLDER, problemFolder,
                AUTHOR_DECISIONS_FOLDER);
        return FilesUtil.getSubfolderNames(authorDecisionFolderPath);
    }

    @Override
    public CodeSupplier getAuthorDecisionCodeSupplier(String problemFolder, String authorDecisionFolder) {
        Path authorDecisionFolderPath = Paths.get(rootPath, PROBLEMS_FOLDER, problemFolder,
                AUTHOR_DECISIONS_FOLDER, authorDecisionFolder);
        if (Files.notExists(authorDecisionFolderPath)) {
            return null;
        }
        return new StandardCodeSupplier(authorDecisionFolderPath);
    }

    @Override
    public AuthorDecisionDescriptor getAuthorDecisionDescriptor(String problemFolder, String authorDecisionFolder) {
        Path descriptorPath = Paths.get(rootPath, PROBLEMS_FOLDER, problemFolder,
                AUTHOR_DECISIONS_FOLDER, authorDecisionFolder, AUTHOR_DECISION_DESCRIPTOR);
        if (Files.notExists(descriptorPath)) {
            return null;
        }
        return AuthorDecisionDescriptor.getExistingAuthorDecisionDescriptor(descriptorPath);
    }
    
    // ------------------------------------------------------------
    // Methods to work with submissions folders
    // ------------------------------------------------------------
    
    @Override
    public String addSubmissionFolder() {
        Path submissionsFolder = Paths.get(rootPath, SUBMISSIONS_FOLDER);
        Path newFolderPath = FilesUtil.addNewNumberSubfolder(submissionsFolder);
        if (newFolderPath == null) {
            return null;
        }
        Path submissionDescriptorPath = Paths.get(newFolderPath.toString(), SUBMISSION_DESCRIPTOR_NAME);
        SubmissionDescriptor.getEmptySubmissionDescriptor(submissionDescriptorPath);
        return newFolderPath.getFileName().toString();
    }
    
    @Override
    public void deleteSubmissionFolder(String submissionFolder) {
        Path submissionFolderPath = Paths.get(rootPath, SUBMISSIONS_FOLDER, submissionFolder);
        FilesUtil.deleteRecursively(submissionFolderPath);
    }
    
    @Override
    public List<String> getSubmissionsFolderNames() {
        Path submissionsFolder = Paths.get(rootPath, SUBMISSIONS_FOLDER);
        return FilesUtil.getSubfolderNames(submissionsFolder);
    }
    
    @Override
    public SubmissionDescriptor getSubmissionDescriptor(String submissionFolder) {
        Path descriptorPath = Paths.get(rootPath, SUBMISSIONS_FOLDER, submissionFolder, SUBMISSION_DESCRIPTOR_NAME);
        if (Files.notExists(descriptorPath)) {
            return null;
        }
        return SubmissionDescriptor.getExistingSubmissionDescriptor(descriptorPath);
    }

    @Override
    public CodeSupplier getSubmissionCodeSupplier(String submissionFolder) {
        Path submissionFolderPath = Paths.get(rootPath, SUBMISSIONS_FOLDER, submissionFolder);
        if (Files.notExists(submissionFolderPath)) {
            return null;
        }
        return new StandardCodeSupplier(submissionFolderPath);
    }
    
    // ------------------------------------------------------------
    // Methods to work with temp files folders
    // ------------------------------------------------------------

    @Override
    public Path getTempFile() {
        Path tempPath = Paths.get(rootPath, TEMP_FILES_FOLDER);
        if (!FilesUtil.checkFolderExists(tempPath)) {
            return null;
        }
        try {
            return Files.createTempFile(tempPath, null, ".txt");
        } catch (IOException exception) {
            FileSystemLogging.logger.log(Level.FINE, "IOException while creating temp file", exception);
            return null;
        }
    }

    @Override
    public void deleteTempFile(Path path) {
        Path tempPath = Paths.get(rootPath, TEMP_FILES_FOLDER);
        if (!FilesUtil.checkFolderExists(tempPath) || path == null) {
            return;
        }
        try {
            Files.deleteIfExists(path);
        } catch (IOException exception) {
            FileSystemLogging.logger.log(Level.FINE, "IOException while deleting temp file", exception);
        }
    }

    @Override
    public void deleteAllTempFiles() {
        Path tempPath = Paths.get(rootPath, TEMP_FILES_FOLDER);
        if (!FilesUtil.checkFolderExists(tempPath)) {
            return;
        }
        FilesUtil.processFolderContent(tempPath, path -> {
            Files.deleteIfExists(path);
        });
    }
    
    // ------------------------------------------------------------
    // Method to work with configuration folder
    // ------------------------------------------------------------

    @Override
    public Path getConfigurationFolder() {
        Path configurationPath = Paths.get(rootPath, CONFIGURATION_FOLDER);
        if (!FilesUtil.checkFolderExists(configurationPath)) {
            return null;
        }
        return configurationPath;
    }
    
}