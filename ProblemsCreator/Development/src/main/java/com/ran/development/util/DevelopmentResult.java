package com.ran.development.util;

public class DevelopmentResult {
    
    private static final String OK_MESSAGE = "Process finished successfully";
    
    public static final int OK = 0,
                            FAIL = 1,
                            INTERRUPTED = 2,
                            DID_NOT_RUN = 3;
    
    private int taskNumber;
    private String message;
    private int info;
    private Long time;

    public DevelopmentResult(int taskNumber, String message, int info, Long time) {
        this.taskNumber = taskNumber;
        this.message = message;
        this.info = info;
        this.time = time;
    }

    public DevelopmentResult(int taskNumber, String message, int info) {
        this(taskNumber, message, info, null);
    }

    public DevelopmentResult(int taskNumber, Long time) {
        this(taskNumber, OK_MESSAGE, OK, time);
    }

    public int getTaskNumber() {
        return taskNumber;
    }

    public String getMessage() {
        return message;
    }

    public int getInfo() {
        return info;
    }

    public Long getTime() {
        return time;
    }
    
}