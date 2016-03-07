package com.ran.development.valid;

public class ValidationException extends Exception {

    private StrictInput.State state = null;
    
    public ValidationException() { }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }
    
    public ValidationException(StrictInput.State state) {
        this.state = state;
    }

    public ValidationException(String message, StrictInput.State state) {
        super(message);
        this.state = state;
    }

    public ValidationException(String message, Throwable cause, StrictInput.State state) {
        super(message, cause);
        this.state = state;
    }

    public ValidationException(Throwable cause, StrictInput.State state) {
        super(cause);
        this.state = state;
    }
    
    public StrictInput.State getStrictInputState() {
        return state;
    }
    
}