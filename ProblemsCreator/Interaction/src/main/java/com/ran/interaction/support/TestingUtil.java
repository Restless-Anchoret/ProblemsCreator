package com.ran.interaction.support;

import com.ran.filesystem.descriptor.AuthorDecisionDescriptor;
import com.ran.filesystem.descriptor.ProblemDescriptor;
import com.ran.filesystem.descriptor.SubmissionDescriptor;
import com.ran.filesystem.descriptor.TestGroupDescriptor;
import com.ran.filesystem.supplier.FileSupplier;
import com.ran.testing.checker.Checker;
import com.ran.testing.checker.CheckerRegistry;
import com.ran.testing.evaluation.EvaluationSystem;
import com.ran.testing.evaluation.EvaluationSystemRegistry;
import com.ran.testing.language.LanguageToolkit;
import com.ran.testing.language.LanguageToolkitRegistry;
import com.ran.testing.system.CodeFileSupplier;
import com.ran.testing.system.ProblemFileSupplier;
import com.ran.testing.system.TestGroupType;
import com.ran.testing.system.TestResultHandler;
import com.ran.testing.system.TestTable;
import com.ran.testing.system.TestingInfo;
import com.ran.testing.tester.ProblemTester;
import com.ran.testing.tester.ProblemTesterRegistry;
import java.util.Properties;

public class TestingUtil {
    
    public static final String PROBLEM_TESTER_ID_DEFAULT =
            ProblemTesterRegistry.CODING_ID;
    public static final boolean PRETESTS_ONLY_DEFAULT = false;
    public static final String EVALUATION_SYSTEM_ID_FOR_AUTHOR_DECISIONS =
            EvaluationSystemRegistry.CHECK_ID;
    
    public static Integer safeConvertToInt(Short value) {
        return (value == null ? null : value.intValue());
    }
    
    public static Short safeConvertToShort(Integer value) {
        return (value == null ? null : value.shortValue());
    }
    
    public static String getTimeDescription(Integer time) {
        return (time == null ? "" : time + " ms");
    }
    
    public static String getMemoryDescription(Integer memory) {
        return (memory == null ? "" : memory + " MB");
    }
    
    public static String getVerdictDescription(String verdict, Integer points,
            Integer wrongTestNumber) {
        Properties properties = PresentationSupport.getPresentationProperties();
        String description = properties.getProperty(verdict, "");
        if (points != null) {
            description += " with " + points + " points";
        } else if (wrongTestNumber!= null) {
            description += " on test " + wrongTestNumber;
        }
        return description;
    }
    
    public static TestingInfo prepareTestingInfoForSubmission(FileSupplier fileSupplier,
            String submissionFolder, TestResultHandler testResultHandler) {
        SubmissionDescriptor submissionDescriptor = fileSupplier.getSubmissionDescriptor(submissionFolder);
        String problemFolder = submissionDescriptor.getProblemName();
        ProblemDescriptor problemDescriptor = fileSupplier.getProblemDescriptor(problemFolder);
        ProblemTester problemTester = ProblemTesterRegistry.registry().get(PROBLEM_TESTER_ID_DEFAULT);
        EvaluationSystem evaluationSystem = EvaluationSystemRegistry.registry().get(submissionDescriptor.getEvaluationType());
        LanguageToolkit languageToolkit = LanguageToolkitRegistry.registry().get(submissionDescriptor.getCompilatorName());
        Checker checker = CheckerRegistry.registry().get(problemDescriptor.getCheckerType());
        CodeFileSupplier codeFileSupplier = new CodeFileSupplierImpl(fileSupplier.getSubmissionCodeSupplier(submissionFolder));
        ProblemFileSupplier problemFileSupplier = new ProblemFileSupplierImpl(problemFolder, fileSupplier);
        TestTable testTable = prepareTestTable(fileSupplier, problemFolder);
        return new TestingInfo(testResultHandler, problemTester, evaluationSystem, languageToolkit, checker,
                codeFileSupplier, problemFileSupplier, PRETESTS_ONLY_DEFAULT, problemDescriptor.getTimeLimit(),
                problemDescriptor.getMemoryLimit().shortValue(), testTable);
    }
    
    public static TestingInfo prepareTestingInfoForAuthorDecision(FileSupplier fileSupplier,
            String problemFolder, String authorDecisionFolder, TestResultHandler testResultHandler) {
        ProblemDescriptor problemDescriptor = fileSupplier.getProblemDescriptor(problemFolder);
        AuthorDecisionDescriptor authorDecisionDescriptor = fileSupplier
                .getAuthorDecisionDescriptor(problemFolder, authorDecisionFolder);
        ProblemTester problemTester = ProblemTesterRegistry.registry().get(PROBLEM_TESTER_ID_DEFAULT);
        EvaluationSystem evaluationSystem = EvaluationSystemRegistry.registry().get(EVALUATION_SYSTEM_ID_FOR_AUTHOR_DECISIONS);
        LanguageToolkit languageToolkit = LanguageToolkitRegistry.registry().get(authorDecisionDescriptor.getCompilatorName());
        Checker checker = CheckerRegistry.registry().get(problemDescriptor.getCheckerType());
        CodeFileSupplier codeFileSupplier = new CodeFileSupplierImpl(fileSupplier
                .getAuthorDecisionCodeSupplier(problemFolder, authorDecisionFolder));
        ProblemFileSupplier problemFileSupplier = new ProblemFileSupplierImpl(problemFolder, fileSupplier);
        TestTable testTable = prepareTestTable(fileSupplier, problemFolder);
        return new TestingInfo(testResultHandler, problemTester, evaluationSystem, languageToolkit, checker,
                codeFileSupplier, problemFileSupplier, PRETESTS_ONLY_DEFAULT, problemDescriptor.getTimeLimit(),
                problemDescriptor.getMemoryLimit().shortValue(), testTable);
    }
    
    private static TestTable prepareTestTable(FileSupplier fileSupplier, String problemFolder) {
        TestTable testTable = new TestTable();
        for (TestGroupType type: TestGroupType.values()) {
            TestGroupDescriptor descriptor = fileSupplier
                    .getTestGroupDescriptor(problemFolder, type.toString().toLowerCase());
            if (descriptor != null) {
                testTable.putTestGroup(type, descriptor.getPointsForTest().shortValue(),
                        fileSupplier.getTestsQuantity(problemFolder, type.toString().toLowerCase()));
            }
        }
        return testTable;
    }
    
    private TestingUtil() { }
    
}