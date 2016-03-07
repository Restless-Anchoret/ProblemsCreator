package com.ran.development.util;

public class DevelopmentResult {
    
    private static final String OK_MESSAGE = "Process finished successfully";
    
    private int taskNumber;
    private String message;
    private Info info;
    private Long time;

    public DevelopmentResult(int taskNumber, String message, Info info, Long time) {
        this.taskNumber = taskNumber;
        this.message = message;
        this.info = info;
        this.time = time;
    }

    public DevelopmentResult(int taskNumber, String message, Info info) {
        this(taskNumber, message, info, null);
    }

    public DevelopmentResult(int taskNumber, Long time) {
        this(taskNumber, OK_MESSAGE, Info.OK, time);
    }

    public int getTaskNumber() {
        return taskNumber;
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