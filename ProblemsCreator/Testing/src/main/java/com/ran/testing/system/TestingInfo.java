package com.ran.testing.system;

import com.ran.testing.checker.Checker;
import com.ran.testing.evaluation.EvaluationSystem;
import com.ran.testing.language.LanguageToolkit;
import com.ran.testing.tester.ProblemTester;

public class TestingInfo {

    private TestResultHandler testResultHandler;
    private ProblemTester problemTester;
    private EvaluationSystem evaluationSystem;
    private LanguageToolkit languageToolkit;
    private Checker checker;
    private String submissionFolder, problemFolder;
    private boolean pretestsOnly;
    private Integer timeLimit;
    private Short memoryLimit;
    private TestTable testTable;
    private VerdictInfo verdictInfo = null;

    public TestingInfo(TestResultHandler testResultHandler, ProblemTester problemTester,
            EvaluationSystem evaluationSystem, LanguageToolkit languageToolkit,Checker checker,
            String submissionFolder, String problemFolder, boolean pretestsOnly, Integer timeLimit,
            Short memoryLimit, TestTable testTable) {
        this.testResultHandler = testResultHandler;
        this.problemTester = problemTester;
        this.evaluationSystem = evaluationSystem;
        this.languageToolkit = languageToolkit;
        this.checker = checker;
        this.submissionFolder = submissionFolder;
        this.problemFolder = problemFolder;
        this.pretestsOnly = pretestsOnly;
        this.timeLimit = timeLimit;
        this.memoryLimit = memoryLimit;
        this.testTable = testTable;
    }

    public TestResultHandler getTestResultHandler() {
        return testResultHandler;
    }

    public ProblemTester getProblemTester() {
        return problemTester;
    }

    public EvaluationSystem getEvaluationSystem() {
        return evaluationSystem;
    }

    public LanguageToolkit getLanguageToolkit() {
        return languageToolkit;
    }

    public Checker getChecker() {
        return checker;
    }

    public String getSubmissionFolder() {
        return submissionFolder;
    }

    public String getProblemFolder() {
        return problemFolder;
    }

    public boolean isPretestsOnly() {
        return pretestsOnly;
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public Short getMemoryLimit() {
        return memoryLimit;
    }

    public TestTable getTestTable() {
        return testTable;
    }

    public VerdictInfo getVerdictInfo() {
        return verdictInfo;
    }

    public void setVerdictInfo(VerdictInfo verdictInfo) {
        this.verdictInfo = verdictInfo;
    }

}