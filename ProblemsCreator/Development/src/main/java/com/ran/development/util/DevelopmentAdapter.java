package com.ran.development.util;

public class DevelopmentAdapter implements DevelopmentListener {

    @Override
    public void processingStarted() { }

    @Override
    public void taskProcessingStarted(int taskNumber) { }

    @Override
    public void taskIsProcessing(int taskNumber, long timeFromStart) { }

    @Override
    public void taskIsDone(DevelopmentResult result) { }

    @Override
    public void processingFinished() { }

}