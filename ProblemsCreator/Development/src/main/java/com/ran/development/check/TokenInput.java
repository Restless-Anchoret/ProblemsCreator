package com.ran.development.check;

import com.ran.development.logging.DevelopmentLogging;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.logging.Level;

public class TokenInput {

    private static TokenInput emptyInput = new TokenInput(new InputStream() {
        @Override
        public int read() throws IOException {
            return -1;
        }
    }, Checker.FAIL);
    
    public static TokenInput getEmptyInput() {
        return emptyInput;
    }
    
    private BufferedReader reader;
    private StringTokenizer tokenizer = null;
    private int failureResultInfo;
    
    public TokenInput(InputStream inputStream, int failureResultInfo) {
        this.reader = new BufferedReader(new InputStreamReader(inputStream));
        this.failureResultInfo = failureResultInfo;
    }
    
    public boolean checkEof() throws CheckResultException {
        String nextLine = null;
        do {
            nextLine = nextLineFromInput();
        } while (nextLine != null && !nextLine.matches(".*\\S.*"));
        return nextLine == null;
    }
    
    public int nextInt() throws CheckResultException {
        try {
            return Integer.parseInt(nextToken());
        } catch (NumberFormatException exception) {
            throw new CheckResultException(failureResultInfo,
                    "Integer number was expected", exception);
        }
    }
    
    public long nextLong() throws CheckResultException {
        try {
            return Long.parseLong(nextToken());
        } catch (NumberFormatException exception) {
            throw new CheckResultException(failureResultInfo,
                    "Long number was expected", exception);
        }
    }
    
    public double nextDouble() throws CheckResultException {
        try {
            return Integer.parseInt(nextToken());
        } catch (NumberFormatException exception) {
            throw new CheckResultException(failureResultInfo,
                    "Double number was expected", exception);
        }
    }
    
    public String nextLine() throws CheckResultException {
        tokenizer = null;
        String nextLine = nextLineFromInput();
        if (nextLine == null) {
            throw new CheckResultException(failureResultInfo, "Unexpected end of file");
        }
        return nextLine;
    }
    
    public String nextToken() throws CheckResultException {
        if (tokenizer == null || !tokenizer.hasMoreTokens()) {
            String nextLine = null;
            do {
                nextLine = nextLineFromInput();
            } while (nextLine != null && !nextLine.matches(".*\\S.*"));
            if (nextLine == null) {
                throw new CheckResultException(failureResultInfo, "Unexpected end of file");
            }
            tokenizer = new StringTokenizer(nextLine);
        }
        return tokenizer.nextToken();
    }
    
    private String nextLineFromInput() throws CheckResultException {
        try {
            return reader.readLine();
        } catch (IOException exception) {
            String message = "IOException while checker's work";
            DevelopmentLogging.logger.log(Level.FINE, message, exception);
            throw new CheckResultException(Checker.FAIL, message, exception);
        }
    }
    
}