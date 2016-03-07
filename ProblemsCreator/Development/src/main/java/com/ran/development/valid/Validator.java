package com.ran.development.valid;

import com.ran.development.util.RegularExpressionParser;
import java.io.InputStream;
import java.util.Objects;

public abstract class Validator {

    public static Validator getDefault() {
        return new Validator() {
            @Override
            public void validate(String[] args) throws ValidationException { }
        };
    }
    
    private StrictInput input = StrictInput.getEmptyInput();
    private RegularExpressionParser parser = new RegularExpressionParser();

    public Validator() { }
    
    public Validator(InputStream inputStream) {
        this.input = new StrictInput(inputStream);
    }

    protected StrictInput getInput() {
        return input;
    }

    protected RegularExpressionParser getParser() {
        return parser;
    }

    public void setInputStream(InputStream inputStream) {
        input = new StrictInput(inputStream);
    }
    
    protected void error() throws ValidationException {
        throw new ValidationException(input.getState());
    }
    
    protected void error(String message) throws ValidationException {
        throw new ValidationException(message, input.getState());
    }
    
    protected void inBounds(int lowEdge, int number, int highEdge) throws ValidationException {
        if (!(lowEdge <= number && number <= highEdge)) {
            error("Number out of bounds");
        }
    }
    
    protected void inBounds(long lowEdge, long number, long highEdge) throws ValidationException {
        if (!(lowEdge <= number && number <= highEdge)) {
            error("Number out of bounds");
        }
    }
    
    protected void inBounds(double lowEdge, double number, double highEdge) throws ValidationException {
        if (!(lowEdge <= number && number <= highEdge)) {
            error("Number out of bounds");
        }
    }
    
    protected void matchesExpression(char symbol, String regularExpression) throws ValidationException {
        if (!parser.matchesExpression(symbol, regularExpression)) {
            error("Symbol does not match the regular expression");
        }
    }
    
    protected void matchesExpression(String line, String regularExpression) throws ValidationException {
        if (!parser.matchesExpression(line, regularExpression)) {
            error("Line does not match the regular expression");
        }
    }
    
    protected void matchesExpression(String[] lines, String regularExpression) throws ValidationException {
        for (String line: lines) {
            if (!parser.matchesExpression(line, regularExpression)) {
                error("Line does not match the regular expression");
            }
        }
    }
    
    protected void inValues(int number, int[] values) throws ValidationException {
        for (int value: values) {
            if (value == number) {
                return;
            }
        }
        error("Number is not in values array");
    }
    
    protected void inValues(long number, long[] values) throws ValidationException {
        for (long value: values) {
            if (value == number) {
                return;
            }
        }
        error("Number is not in values array");
    }
    
    protected void inValues(double number, double[] values, double epsilon) throws ValidationException {
        for (double value: values) {
            if (Math.abs(number - value) <= epsilon) {
                return;
            }
        }
        error("Number is not in values array");
    }
    
    protected void inValues(char symbol, char[] values) throws ValidationException {
        for (char value: values) {
            if (value == symbol) {
                return;
            }
        }
        error("Symbol is not in values array");
    }
    
    protected <T> void inValues(T object, T[] values) throws ValidationException {
        for (T value: values) {
            if (Objects.equals(object, value)) {
                return;
            }
        }
        error("Object is not in values array");
    }
    
    public void performValidation(String[] args) throws ValidationException {
        validate(args);
        if (!input.isReaden()) {
            error("File was not readen completely");
        }
    }
    
    public abstract void validate(String[] args) throws ValidationException;
    
}