package com.ran.filesystem.descriptor;

import java.nio.file.Path;

public class SubmissionDescriptor extends EntityDescriptor {

    private static final String ROOT_ELEMENT = "submission",
//                                SUBMISSION_NAME = "name",
                                PROBLEM_NAME = "problem",
                                EVALUATION_TYPE = "evaluation",
                                COMPILATOR_NAME = "compilator",
                                VERDICT = "verdict",
                                WRONG_TEST_NUMBER = "wrongTestNumber",
                                DECISION_TIME = "decisionTime",
                                DECISION_MEMORY = "decisionMemory",
                                POINTS = "points";
    
    public static SubmissionDescriptor getEmptySubmissionDescriptor(Path path) {
        SubmissionDescriptor descriptor = new SubmissionDescriptor(path);
        descriptor.persist();
        return descriptor;
    }

    public static SubmissionDescriptor getExistingSubmissionDescriptor(Path path) {
        SubmissionDescriptor descriptor = new SubmissionDescriptor(path);
        descriptor.load();
        return descriptor;
    }

    private SubmissionDescriptor(Path path) {
        super(path, ROOT_ELEMENT);
//        setProperty(SUBMISSION_NAME, "");
        setProperty(PROBLEM_NAME, "");
        setProperty(EVALUATION_TYPE, "");
        setProperty(COMPILATOR_NAME, "");
        setProperty(VERDICT, "");
        setProperty(WRONG_TEST_NUMBER, "");
        setProperty(DECISION_TIME, "");
        setProperty(DECISION_MEMORY, "");
        setProperty(POINTS, "");
    }

//    public String getSubmissionName() {
//        return getProperty(SUBMISSION_NAME);
//    }
//    
//    public void setSubmissionName(String submissionName) {
//        setProperty(SUBMISSION_NAME, submissionName);
//    }

    public String getProblemName() {
        return getProperty(PROBLEM_NAME);
    }
    
    public void setProblemName(String problemName) {
        setProperty(PROBLEM_NAME, problemName);
    }
    
    public String getEvaluationType() {
        return getProperty(EVALUATION_TYPE);
    }
    
    public void setEvaluationType(String evaluationType) {
        setProperty(EVALUATION_TYPE, evaluationType);
    }

    public String getCompilatorName() {
        return getProperty(COMPILATOR_NAME);
    }
    
    public void setCompilatorName(String compilatorName) {
        setProperty(COMPILATOR_NAME, compilatorName);
    }

    public String getVerdict() {
        return getProperty(VERDICT);
    }

    public void setVerdict(String verdict) {
        setProperty(VERDICT, verdict);
    }

    public Integer getWrongTestNumber() {
        return parseInt(getProperty(WRONG_TEST_NUMBER));
    }

    public void setWrongTestNumber(Integer number) {
        setProperty(WRONG_TEST_NUMBER, intToString(number));
    }

    public Integer getDecisionTime() {
        return parseInt(getProperty(DECISION_TIME));
    }

    public void setDecisionTime(Integer decisionTime) {
        setProperty(DECISION_TIME, intToString(decisionTime));
    }

    public Integer getDecisionMemory() {
        return parseInt(getProperty(DECISION_MEMORY));
    }

    public void setDecisionMemory(Integer decisionMemory) {
        setProperty(DECISION_MEMORY, intToString(decisionMemory));
    }

    public Integer getDecisionPoints() {
        return parseInt(getProperty(POINTS));
    }

    public void setDecisionPoints(Integer points) {
        setProperty(POINTS, intToString(points));
    }

}
