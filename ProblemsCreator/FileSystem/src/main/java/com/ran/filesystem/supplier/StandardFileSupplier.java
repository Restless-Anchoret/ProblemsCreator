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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class StandardFileSupplier implements FileSupplier {

    private static final String FILE_SYSTEM_FOLDER = "file_system",
                                PROBLEMS_FOLDER = "problems",
                                TESTS_FOLDER = "tests",
                                INPUT_FILE_NAME = "input.txt",
                                ANSWER_FILE_NAME = "output.txt",
                                TEST_GROUP_DESCRIPTOR = "test_group.xml",
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
                                CONFIGURATION_FOLDER = "config";
    private static final List<String> TEST_GROUP_NAMES = Arrays.asList(
            "samples", "pretests", "tests_1", "tests_2", "tests_3",
            "tests_4", "tests_5", "tests_6", "tests_7", "tests_8");
    
    private String rootPath;

    public StandardFileSupplier(Path path) {
        this.rootPath = Paths.get(path.toAbsolutePath().toString(), FILE_SYSTEM_FOLDER).toString();
    }

    // ------------------------------------------------------------
    // Methods to work with problem folders
    // ------------------------------------------------------------
    
    @Override
    public String addProblemFolder() {
        return null;
    }
    
    @Override
    public void deleteProblemFolder(String problemFolder) {
    }

    @Override
    public List<String> getProblemsFolderNames() {
        return null;
    }

    @Override
    public ProblemDescriptor getProblemDescriptor(String problemFolder) {
        return null;
    }

    @Override
    public Path getProblemStatementPath(String problemFolder) {
        return null;
    }

    // ------------------------------------------------------------
    // Methods to work with tests folders
    // ------------------------------------------------------------
    
    @Override
    public void addTestInputFiles(String problemFolder, String testGroupType, List<Path> inputFilePaths) {
    }

    @Override
    public void addTestAnswerFile(String problemFolder, String testGroupType, int testNumber, Path answerFilePath) {
    }

    @Override
    public void deleteTests(String problemFolder, String testGroupType, List<Integer> testNumbers) {
    }

    @Override
    public Path getTestInputFile(String problemFolder, String testGroupType, int testNumber) {
        return null;
    }

    @Override
    public Path getTestAnswerFile(String problemFolder, String testGroupType, int testNumber) {
        return null;
    }
    
    @Override
    public int getTestsQuantity(String problemFolder, String testGroupType) {
        return 0;
    }

    @Override
    public TestGroupDescriptor getTestGroupDescriptor(String problemFolder, String testGroupType) {
        return null;
    }
    
    // ------------------------------------------------------------
    // Method to work with checker folders
    // ------------------------------------------------------------
    
    @Override
    public CodeSupplier getCheckerCodeSupplier(String problemFolder) {
        return null;
    }
    
    // ------------------------------------------------------------
    // Methods to work with generator folders
    // ------------------------------------------------------------

    @Override
    public String addGeneratorFolder(String problemFolder) {
        return null;
    }

    @Override
    public void deleteGeneratorFolder(String problemFolder, String generatorFolder) {
    }

    @Override
    public List<String> getGeneratorFolders(String problemFolder) {
        return null;
    }

    @Override
    public CodeSupplier getGeneratorCodeSupplier(String problemFolder, String generatorFolder) {
        return null;
    }
    
    // ------------------------------------------------------------
    // Methods to work with validator folders
    // ------------------------------------------------------------
    
    @Override
    public String addValidatorFolder(String problemFolder) {
        return null;
    }

    @Override
    public void deleteValidatorFolder(String problemFolder, String validatorFolder) {
    }

    @Override
    public List<String> getValidatorFolders(String problemFolder) {
        return null;
    }

    @Override
    public CodeSupplier getValidatorCodeSupplier(String problemFolder, String validatorFolder) {
        return null;
    }
    
    // ------------------------------------------------------------
    // Methods to work with author decisions folders
    // ------------------------------------------------------------
    
    @Override
    public String addAuthorDecisionFolder(String problemFolder) {
        return null;
    }
    
    @Override
    public void deleteAuthorDecisionFolder(String problemFolder, String authorDecisionFolder) {
    }
    
    @Override
    public List<String> getAuthorDecisionsFolderNames(String problemFolder) {
        return null;
    }

    @Override
    public CodeSupplier getAuthorDecisionCodeSupplier(String problemFolder, String authorDecisionFolder) {
        return null;
    }

    @Override
    public AuthorDecisionDescriptor getAuthorDecisionDescriptor(String problemFolder, String authorDecisionFolder) {
        return null;
    }
    
    // ------------------------------------------------------------
    // Methods to work with submissions folders
    // ------------------------------------------------------------
    
    @Override
    public String addSubmissionFolder() {
        Path submissionsFolder = Paths.get(rootPath, SUBMISSIONS_FOLDER);
        if (!FilesUtil.checkFolderExists(submissionsFolder)) {
            return null;
        }
        String newFolderName = Integer.toString(FilesUtil.getMaximumFolderNameNumber(submissionsFolder) + 1);
        Path newFolderPath = Paths.get(submissionsFolder.toString(), newFolderName);
        if (!FilesUtil.checkFolderExists(newFolderPath)) {
            return null;
        }
        Path submissionDescriptorPath = Paths.get(newFolderPath.toString(), SUBMISSION_DESCRIPTOR_NAME);
        SubmissionDescriptor.getEmptySubmissionDescriptor(submissionDescriptorPath);
        return newFolderName;
    }
    
    @Override
    public void deleteSubmissionFolder(String submissionFolder) {
    }
    
    @Override
    public List<String> getSubmissionsFolderNames() {
        List<String> folderNames = new ArrayList<>();
        Path submissionsFolder = Paths.get(rootPath, SUBMISSIONS_FOLDER);
        if (!FilesUtil.checkFolderExists(submissionsFolder)) {
            return folderNames;
        }
        FilesUtil.processFolderContent(submissionsFolder, path -> {
            if (Files.isDirectory(path)) {
                folderNames.add(path.getFileName().toString());
            }
        });
        return folderNames;
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
        FilesUtil.checkFolderExists(configurationPath);
        return configurationPath;
    }
    
}