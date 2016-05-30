package com.ran.filesystem.supplier;

import com.ran.filesystem.descriptor.AuthorDecisionDescriptor;
import com.ran.filesystem.descriptor.ProblemDescriptor;
import com.ran.filesystem.descriptor.SubmissionDescriptor;
import com.ran.filesystem.descriptor.TestGroupDescriptor;
import java.nio.file.Path;
import java.util.List;

public interface FileSupplier {

    String addProblemFolder();
    void deleteProblemFolder(String problemFolder);
    List<String> getProblemsFolderNames();
    ProblemDescriptor getProblemDescriptor(String problemFolder);
    Path getProblemStatementPath(String problemFolder);
    boolean putProblemStatementPath(String problemFolder, Path newStatementPath);
    
    boolean addTestInputFiles(String problemFolder, String testGroupType, List<Path> inputFilePaths);
    boolean addTestAnswerFile(String problemFolder, String testGroupType, int testNumber, Path answerFilePath);
    void deleteTests(String problemFolder, String testGroupType, List<Integer> testNumbers);
    Path getTestInputFile(String problemFolder, String testGroupType, int testNumber);
    Path getTestAnswerFile(String problemFolder, String testGroupType, int testNumber);
    int getTestsQuantity(String problemFolder, String testGroupType);
    TestGroupDescriptor getTestGroupDescriptor(String problemFolder, String testGroupType);
    
    CodeSupplier getCheckerCodeSupplier(String problemFolder);

    String addGeneratorFolder(String problemFolder);
    void deleteGeneratorFolder(String problemFolder, String generatorFolder);
    List<String> getGeneratorFolders(String problemFolder);
    CodeSupplier getGeneratorCodeSupplier(String problemFolder, String generatorFolder);
    
    String addValidatorFolder(String problemFolder);
    void deleteValidatorFolder(String problemFolder, String validatorFolder);
    List<String> getValidatorFolders(String problemFolder);
    CodeSupplier getValidatorCodeSupplier(String problemFolder, String validatorFolder);

    String addAuthorDecisionFolder(String problemFolder);
    void deleteAuthorDecisionFolder(String problemFolder, String authorDecisionFolder);
    List<String> getAuthorDecisionsFolderNames(String problemFolder);
    CodeSupplier getAuthorDecisionCodeSupplier(String problemFolder, String authorDecisionFolder);
    AuthorDecisionDescriptor getAuthorDecisionDescriptor(String problemFolder, String authorDecisionFolder);

    String addSubmissionFolder();
    void deleteSubmissionFolder(String submissionFolder);
    List<String> getSubmissionsFolderNames();
    SubmissionDescriptor getSubmissionDescriptor(String submissionFolder);
    CodeSupplier getSubmissionCodeSupplier(String submissionFolder);

    Path getTempFile();
    void deleteTempFile(Path path);
    void deleteAllTempFiles();

    Path getConfigurationFolder();
    
}
