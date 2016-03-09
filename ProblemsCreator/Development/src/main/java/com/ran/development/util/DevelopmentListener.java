package com.ran.development.util;

public interface DevelopmentListener {

    void processingStarted();
    void taskProcessingStarted(int taskNumber);
    void taskIsProcessing(int taskNumber, long timeFromStart);
    void taskIsDone(DevelopmentResult result);
    void processingFinished();
    
}
