package com.ran.testing.language;

public class FailException extends Exception {

    public FailException() {
    }

    public FailException(String msg) {
        super(msg);
    }
    
    public FailException(Throwable cause) {
        super(cause);
    }
    
    public FailException(String msg, Throwable cause) {
        super(msg, cause);
    }

}