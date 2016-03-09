package com.ran.development.valid;

import com.ran.development.logging.DevelopmentLogging;
import com.ran.development.util.SingleCharReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class StrictInput {

    private static StrictInput emptyInput = new StrictInput(new InputStream() {
        @Override
        public int read() throws IOException {
            return -1;
        }
    });
    
    public static StrictInput getEmptyInput() {
        return emptyInput;
    }

    private SingleCharReader reader;
    private int position = 1, lineNumber = 1;
    private boolean readen = false;

    public StrictInput(InputStream inputStream) {
        this.reader = new SingleCharReader(new BufferedReader(new InputStreamReader(inputStream)));
    }

    public boolean isReaden() {
        return readen;
    }

    public void readSpace() throws ValidationException {
        int symbol = nextSymbol();
        if (symbol == -1 || (char)symbol != ' ') {
            throw new ValidationException("Space symbol was expected", getState());
        }
    }

    public void readSpace(int times) throws ValidationException {
        for (int i = 0; i < times; i++) {
            readSpace();
        }
    }

    public void readln() throws ValidationException {
        int symbol = nextSymbol();
        if (symbol == -1 || (char)symbol != '\n') {
            throw new ValidationException("End of line was expected", getState());
        }
    }

    public void readln(int times) throws ValidationException {
        for (int i = 0; i < times; i++) {
            readln();
        }
    }

    public void readEof() throws ValidationException {
        if (peekSymbol() != -1) {
            throw new ValidationException("End of file was expected", getState());
        }
        readen = true;
    }

    public int readInt() throws ValidationException {
        String number = readNumber(true);
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException exception) {
            throw new ValidationException("Integer number was expected", exception, getState());
        }
    }

    public long readLong() throws ValidationException {
        String number = readNumber(true);
        try {
            return Long.parseLong(number);
        } catch (NumberFormatException exception) {
            throw new ValidationException("Long number was expected", exception, getState());
        }
    }

    public double readDouble() throws ValidationException {
        StringBuilder builder = new StringBuilder(readNumber(true));
        int symbol = peekSymbol();
        if (symbol != -1 && (char)symbol == '.') {
            builder.append((char)nextSymbol());
            builder.append(readNumber(false));
        }
        try {
            return Double.parseDouble(builder.toString());
        } catch (NumberFormatException exception) {
            throw new ValidationException("Double number was expected", exception, getState());
        }
    }
    
    private String readNumber(boolean canStartWithMinus) throws ValidationException {
        StringBuilder builder = new StringBuilder();
        do {
            int symbol = peekSymbol();
            if (symbol != -1 && (canStartWithMinus && builder.length() == 0 && (char)symbol == '-'
                    || Character.isDigit((char)symbol))) {
                nextSymbol();
                builder.append((char)symbol);
            } else {
                break;
            }
        } while (true);
        if (builder.length() == 0) {
            throw new ValidationException("Number was expected", getState());
        }
        return builder.toString();
    }

    public char read() throws ValidationException {
        int symbol = nextSymbol();
        if (symbol == -1) {
            throw new ValidationException("Unexpected end of file", getState());
        }
        return (char)symbol;
    }
    
    public String readLine(int symbols) throws ValidationException {
        StringBuilder builder = new StringBuilder(symbols);
        for (int i = 0; i < symbols; i++) {
            builder.append(read());
        }
        return builder.toString();
    }

    public String readLine() throws ValidationException {
        StringBuilder builder = new StringBuilder();
        do {
            int symbol = peekSymbol();
            if (symbol != -1 && (char)symbol != '\n') {
                nextSymbol();
                builder.append((char)symbol);
            } else {
                break;
            }
        } while (true);
        return builder.toString();
    }
    
    private int nextSymbol() throws ValidationException {
        try {
            int symbol = reader.next();
            if (symbol != -1 && (char)symbol == '\n') {
                position = 1;
                lineNumber++;
            } else if (symbol != -1) {
                position++;
            }
            return symbol;
        } catch (IOException exception) {
            String message = "IOException while reading input test";
            DevelopmentLogging.logger.log(Level.FINE, message, exception);
            throw new ValidationException(message, exception, getState());
        }
    }
    
    private int peekSymbol() throws ValidationException {
        try {
            return reader.peek();
        } catch (IOException exception) {
            String message = "IOException while reading input test";
            DevelopmentLogging.logger.log(Level.FINE, message, exception);
            throw new ValidationException(message, exception, getState());
        }
    }
    
    public State getState() {
        if (readen) {
            return State.getReadenState();
        }
        return new State(position, lineNumber);
    }
    
    public static class State {
        
        public static State getReadenState() {
            State state = new State();
            state.position = state.lineNumber = null;
            state.readen = true;
            return state;
        }
        
        private Integer position, lineNumber;
        private boolean readen;

        private State() { }
        
        public State(int position, int lineNumber) {
            this.position = position;
            this.lineNumber = lineNumber;
            readen = false;
        }

        public Integer getPosition() {
            return position;
        }

        public Integer getLineNumber() {
            return lineNumber;
        }

        public boolean isReaden() {
            return readen;
        }
        
    }

}
