package com.ran.filesystem.supplier;

import java.nio.file.Path;
import java.util.List;

public interface FileSupplier {

    String addProblemFolder();
    Path getProblemFolder(String problemFolder);
    Path getProblemStatement(String problemFolder);
    CodeSupplier getProblemCheckerCodeSupplier(String problemFolder);

    Path getTestInputFile(String problemFolder, String testGroupType, int testNumber);
    Path getTestAnswerFile(String problemFolder, String testGroupType, int testNumber);

    String addAuthorDecisionFolder(String problemFolder);
    List<String> getAuthorDecisionsFolderNames(String problemFolder);
    CodeSupplier getAuthorDecisionCodeSupplier(String problemFolder);

    String addSubmissionFolder();
    List<String> getSubmissionsFolderNames();
    SubmissionDescriptor getSubmissionDescriptor(String submissionFolder);
    CodeSupplier getSubmissionCodeSupplier(String submissionFolder);

    Path getTempFile();
    void deleteTempFile(Path path);
    void deleteAllTempFiles();

    Path getConfigurationFolder();
    
}
