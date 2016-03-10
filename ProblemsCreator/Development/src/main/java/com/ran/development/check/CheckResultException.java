package com.ran.development.check;

public class CheckResultException extends Exception {

    private int resultInfo;
    
    public CheckResultException() { }

    public CheckResultException(String message) {
        super(message);
    }

    public CheckResultException(String message, Throwable cause) {
        super(message, cause);
    }

    public CheckResultException(Throwable cause) {
        super(cause);
    }

    public CheckResultException(int resultInfo) {
        this.resultInfo = resultInfo;
    }

    public CheckResultException(int resultInfo, String message) {
        super(message);
        this.resultInfo = resultInfo;
    }

    public CheckResultException(int resultInfo, String message, Throwable cause) {
        super(message, cause);
        this.resultInfo = resultInfo;
    }

    public CheckResultException(int resultInfo, Throwable cause) {
        super(cause);
        this.resultInfo = resultInfo;
    }

    public int getResultInfo() {
        return resultInfo;
    }
    
}