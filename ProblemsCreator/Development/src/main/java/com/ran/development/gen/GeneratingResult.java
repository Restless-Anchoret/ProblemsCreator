package com.ran.development.gen;

public class GeneratingResult {
    
    private static final String OK_MESSAGE = "Generating finished successfully";
    
    private int generatorNumber;
    private String message;
    private Info info;
    private Long time;

    public GeneratingResult(int generatorNumber, String message, Info info, Long time) {
        this.generatorNumber = generatorNumber;
        this.message = message;
        this.info = info;
        this.time = time;
    }

    public GeneratingResult(int generatorNumber, String message, Info info) {
        this(generatorNumber, message, info, null);
    }

    public GeneratingResult(int generatorNumber, Long time) {
        this(generatorNumber, OK_MESSAGE, Info.OK, time);
    }

    public int getGeneratorNumber() {
        return generatorNumber;
    }

    public String getMessage() {
        return message;
    }

    public Info getInfo() {
        return info;
    }

    public Long getTime() {
        return time;
    }

    public static enum Info {
        OK,
        INTERRUPTED,
        FAIL,
        DID_NOT_RUN
    }
    
}