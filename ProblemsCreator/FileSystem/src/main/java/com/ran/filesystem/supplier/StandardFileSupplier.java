package com.ran.filesystem.supplier;

import com.ran.filesystem.logging.FileSystemLogging;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class StandardFileSupplier implements FileSupplier {

    private static final String FILE_SYSTEM_FOLDER = "file_system",
                                SUBMISSIONS_FOLDER = "submissions",
                                SUBMISSION_DESCRIPTOR_NAME = "submission.xml",
                                CONFIGURATION_FOLDER = "config",
                                TEMP_FILES_FOLDER = "temp",
                                PROBLEMS_FOLDER = "problems",
                                PROBLEM_DESCRIPTOR_NAME = "problem.xml",
                                TESTS_FOLDER = "tests",
                                TEST_GROUP_DESCRIPTOR = "test_group.xml",
                                INPUT_FILE_NAME = "input.txt",
                                ANSWER_FILE_NAME = "output.txt",
                                AUTHOR_DECISIONS_FOLDER = "author_decisions",
                                CHECKER_FOLDER = "checker",
                                GENERATORS_FOLDER = "generators",
                                VALIDATORS_FOLDER = "validators",
                                STATEMENT_FILE_NAME = "statement.pdf";
    
    private String rootPath;

    public StandardFileSupplier(Path path) {
        this.rootPath = Paths.get(path.toAbsolutePath().toString(), FILE_SYSTEM_FOLDER).toString();
    }

    @Override
    public String addProblemFolder() {
        return null;
    }

    @Override
    public Path getProblemFolder(String problemFolder) {
        return null;
    }

    @Override
    public Path getProblemStatement(String problemFolder) {
        return null;
    }

    @Override
    public CodeSupplier getProblemCheckerCodeSupplier(String problemFolder) {
        return null;
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
    public String addAuthorDecisionFolder(String problemFolder) {
        return null;
    }
    
    @Override
    public List<String> getAuthorDecisionsFolderNames(String problemFolder) {
        return null;
    }

    @Override
    public CodeSupplier getAuthorDecisionCodeSupplier(String problemFolder) {
        return null;
    }

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

    @Override
    public Path getConfigurationFolder() {
        Path configurationPath = Paths.get(rootPath, CONFIGURATION_FOLDER);
        FilesUtil.checkFolderExists(configurationPath);
        return configurationPath;
    }
    
}