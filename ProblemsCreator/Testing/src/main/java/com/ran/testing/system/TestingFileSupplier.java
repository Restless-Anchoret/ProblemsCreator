package com.ran.testing.system;

import java.nio.file.Path;

public interface TestingFileSupplier {

    Path getSubmissionFolder(String submissionFolder);
    Path getSubmissionSourceFolder(String submissionFolder);
    Path getSubmissionSourceFile(String submissionFolder);
    Path getSubmissionCompileFolder(String submissionFolder);
    Path getSubmissionCompileFile(String submissionFolder);
    Path getConfigurationFolder();

    Path getTempFile();
    void deleteTempFile(Path path);
    void deleteAllTempFiles();

    Path getTestInputFile(String problemFolder, TestGroupType type, int testNumber);
    Path getTestAnswerFile(String problemFolder, TestGroupType type, int testNumber);

}