package com.ran.filesystem.supplier;

import java.nio.file.Path;

public class SubmissionDescriptor extends EntityDescriptor {

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
        super(path, "submission");
        setProperty("name", "");
        setProperty("problem", "");
        setProperty("compilator", "");
        setProperty("verdict", "");
        setProperty("wrongTestNumber", "");
        setProperty("decisionTime", "");
        setProperty("decisionMemory", "");
        setProperty("points", "");
    }

    public String getSubmissionName() {
        return getProperty("name");
    }
    
    public void setSubmissionName(String submissionName) {
        setProperty("name", submissionName);
    }

    public String getProblemName() {
        return getProperty("problem");
    }
    
    public void setProblemName(String problemName) {
        setProperty("problem", problemName);
    }

    public String getCompilatorName() {
        return getProperty("compilator");
    }
    
    public void setCompilatorName(String compilatorName) {
        setProperty("compilator", compilatorName);
    }

    public String getVerdict() {
        return getProperty("verdict");
    }

    public void setVerdict(String verdict) {
        setProperty("verdict", verdict);
    }

    public Integer getWrongTestNumber() {
        return parseInt(getProperty("wrongTestNumber"));
    }

    public void setWrongTestNumber(Integer number) {
        setProperty("wrongTestNumber", intToString(number));
    }

    public Integer getDecisionTime() {
        return parseInt(getProperty("decisionTime"));
    }

    public void setDecisionTime(Integer decisionTime) {
        setProperty("decisionTime", intToString(decisionTime));
    }

    public Integer getDecisionMemory() {
        return parseInt(getProperty("decisionMemory"));
    }

    public void setDecisionMemory(Integer decisionMemory) {
        setProperty("decisionMemory", intToString(decisionMemory));
    }

    public Integer getDecisionPoints() {
        return parseInt(getProperty("points"));
    }

    public void setDecisionPoints(Integer points) {
        setProperty("points", intToString(points));
    }
    
    private Integer parseInt(String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException exception) {
            return null;
        }
    }
    
    private String intToString(Integer number) {
        if (number == null) {
            return "";
        } else {
            return number.toString();
        }
    }

}
