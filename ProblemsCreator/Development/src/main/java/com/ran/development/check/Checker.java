package com.ran.development.check;

import com.ran.development.util.RegularExpressionParser;
import java.io.InputStream;

public abstract class Checker {

    public static final int OK = 0,
            WRONG_ANSWER = 1,
            FAIL = 2;

    private TokenInput input = TokenInput.getEmptyInput();
    private TokenInput output = TokenInput.getEmptyInput();
    private TokenInput answer = TokenInput.getEmptyInput();
    private RegularExpressionParser parser = new RegularExpressionParser();

    public void setInput(InputStream inputStream) {
        input = new TokenInput(inputStream, FAIL);
    }

    public void setOutput(InputStream inputStream) {
        output = new TokenInput(inputStream, WRONG_ANSWER);
    }

    public void setAnswer(InputStream inputStream) {
        answer = new TokenInput(inputStream, FAIL);
    }

    protected TokenInput getInput() {
        return input;
    }

    protected TokenInput getOutput() {
        return output;
    }

    protected TokenInput getAnswer() {
        return answer;
    }

    protected RegularExpressionParser getParser() {
        return parser;
    }

    protected void quit(int resultInfo, String message) throws CheckResultException {
        throw new CheckResultException(resultInfo, message);
    }

    protected void quit(int resultInfo) throws CheckResultException {
        quit(resultInfo, null);
    }

    protected void quitIf(boolean condition, int resultInfo, String message)
            throws CheckResultException {
        if (condition) {
            quit(resultInfo, message);
        }
    }

    protected void quitIf(boolean condition, int resultInfo) throws CheckResultException {
        quitIf(condition, resultInfo, null);
    }

    protected void ensureIf(boolean condition, String message) throws CheckResultException {
        if (!condition) {
            quit(WRONG_ANSWER, message);
        }
    }

    protected void ensureIf(boolean condition) throws CheckResultException {
        ensureIf(condition, null);
    }

    public void performChecking() throws CheckResultException {
        try {
            check();
        } catch (CheckResultException exception) {
            if (exception.getResultInfo() == OK && !output.checkEof()) {
                quit(WRONG_ANSWER, "Extra values in the output");
            }
            throw exception;
        }
        quit(FAIL, "Checker did not tell result of checking");
    }

    public abstract void check() throws CheckResultException;

}
