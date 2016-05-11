package com.ran.testing.temp;

import com.ran.testing.logging.TestingLogging;
import com.ran.testing.system.TestGroupType;
import com.ran.testing.system.TestingFileSupplier;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

public class SimpleFileSupplier implements TestingFileSupplier {
    
    private static final String fileSystemFolder = "file_system";
    private static final String submissionsMainFolder = "submissions";
    private static final String sourceFolder = "src";
    private static final String compilingFolder = "bin";
    private static final String configurationFolder = "config";
    private static final String tempFilesFolder = "temp";
    private static final String problemsMainFolder = "problems";
    private static final String testsFolder = "tests";
    private static final String inputFileName = "input.txt";
    private static final String answerFileName = "output.txt";
    
    public Path getSubmissionFolder(String submissionFolder) {
        return Paths.get(fileSystemFolder, submissionsMainFolder, submissionFolder);
    }

    public Path getSubmissionSourceFolder(String submissionFolder) {
        return Paths.get(fileSystemFolder, submissionsMainFolder, submissionFolder, sourceFolder);
    }

    public Path getSubmissionSourceFile(String submissionFolder) {
        return Paths.get(fileSystemFolder, submissionsMainFolder, submissionFolder, sourceFolder, getSourceFileName(submissionFolder));
    }

    public Path getSubmissionCompileFolder(String submissionFolder) {
        Path path = Paths.get(fileSystemFolder, submissionsMainFolder, submissionFolder, compilingFolder);
        try {
            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }
            return path;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Path getSubmissionCompileFile(String submissionFolder) {
        return Paths.get(fileSystemFolder, submissionsMainFolder, submissionFolder, compilingFolder, getCompileFileName(submissionFolder));
    }
    
    @Override
    public Path getConfigurationFolder() {
        return Paths.get(fileSystemFolder, configurationFolder).toAbsolutePath();
    }

    @Override
    public Path getTempFile() {
        Path tempFilesPath = Paths.get(fileSystemFolder, tempFilesFolder);
        try {
            if (!Files.exists(tempFilesPath)) {
                Files.createDirectory(tempFilesPath);
            }
            return Files.createTempFile(tempFilesPath, null, ".txt");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteTempFile(Path path) {
        if (path == null) {
            return;
        }
        try {
            Files.deleteIfExists(path);
        } catch (IOException exception) {
            TestingLogging.logger.log(Level.FINE, "IOException while deleting temp file", exception);
        }
    }

    @Override
    public void deleteAllTempFiles() {
        try (DirectoryStream<Path> entries = Files.newDirectoryStream(Paths.get(fileSystemFolder, tempFilesFolder))) {
            for (Path path: entries) {
                Files.deleteIfExists(path);
            }
        } catch (IOException exception) {
            TestingLogging.logger.log(Level.FINE, "IOException while deleting temp file", exception);
        }
    }

    public Path getTestInputFile(String problemFolder, TestGroupType type, int testNumber) {
        return Paths.get(fileSystemFolder, problemsMainFolder, problemFolder, testsFolder, type.name().toLowerCase(),
                Integer.toString(testNumber), inputFileName);
    }

    public Path getTestAnswerFile(String problemFolder, TestGroupType type, int testNumber) {
        return Paths.get(fileSystemFolder, problemsMainFolder, problemFolder, testsFolder, type.name().toLowerCase(),
                Integer.toString(testNumber), answerFileName);
    }
    
    private String getSourceFileName(String submissionFolder) {
        if (Integer.parseInt(submissionFolder) > 7) {
            return "source.cpp";
        } else {
            return "A.java";
        }
    }
    
    private String getCompileFileName(String submissionFolder) {
        if (Integer.parseInt(submissionFolder) > 7) {
            return "source.exe";
        } else {
            return "A.class";
        }
    }

}