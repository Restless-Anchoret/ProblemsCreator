package com.ran.development.util;

public interface DevelopmentListener {

    void processingStarted();
    void taskIsProcessing(int generatorNumber, long timeFromStart);
    void taskIsDone(DevelopmentResult result);
    void processingFinished();
    
}
