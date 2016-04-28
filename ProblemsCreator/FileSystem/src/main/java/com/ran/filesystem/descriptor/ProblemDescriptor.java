package com.ran.filesystem.descriptor;

import java.nio.file.Path;

public class ProblemDescriptor extends EntityDescriptor {

    private static final String ROOT_ELEMENT = "problem",
                                PROBLEM_NAME = "name",
                                TIME_LIMIT = "timeLimit",
                                MEMORY_LIMIT = "memoryLimit",
                                CHECKER_TYPE = "checker";
    
    public static ProblemDescriptor getEmptyProblemDescriptor(Path path) {
        ProblemDescriptor descriptor = new ProblemDescriptor(path);
        descriptor.persist();
        return descriptor;
    }

    public static ProblemDescriptor getExistingProblemDescriptor(Path path) {
        ProblemDescriptor descriptor = new ProblemDescriptor(path);
        descriptor.load();
        return descriptor;
    }

    private ProblemDescriptor(Path path) {
        super(path, ROOT_ELEMENT);
        setProperty(PROBLEM_NAME, "New problem");
        setProperty(TIME_LIMIT, "1000");
        setProperty(MEMORY_LIMIT, "64");
        setProperty(CHECKER_TYPE, "match");
    }
    
    public String getProblemName() {
        return getProperty(PROBLEM_NAME);
    }
    
    public void setProblemName(String problemName) {
        setProperty(PROBLEM_NAME, problemName);
    }
    
    public Integer getTimeLimit() {
        return parseInt(getProperty(TIME_LIMIT));
    }
    
    public void setTimeLimit(Integer timeLimit) {
        setProperty(TIME_LIMIT, intToString(timeLimit));
    }
    
    public Integer getMemoryLimit() {
        return parseInt(getProperty(MEMORY_LIMIT));
    }
    
    public void setMemoryLimit(Integer memoryLimit) {
        setProperty(MEMORY_LIMIT, intToString(memoryLimit));
    }
    
    public String getCheckerType() {
        return getProperty(CHECKER_TYPE);
    }
    
    public void setCheckerType(String checkerType) {
        setProperty(CHECKER_TYPE, checkerType);
    }
    
}