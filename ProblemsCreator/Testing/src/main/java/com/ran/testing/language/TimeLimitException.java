package com.ran.testing.language;

public class TimeLimitException extends Exception {

    private Integer decisionTime = null;
    
    public TimeLimitException() {
    }

    public TimeLimitException(String message) {
        super(message);
    }
    
    public TimeLimitException(Integer decisionTime) {
        this.decisionTime = decisionTime;
    }
    
    public TimeLimitException(String message, Integer decisionTime) {
        super(message);
        this.decisionTime = decisionTime;
    }

    public Integer getDecisionTime() {
        return decisionTime;
    }

}