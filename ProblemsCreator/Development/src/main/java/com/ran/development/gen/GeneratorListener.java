package com.ran.development.gen;

public interface GeneratorListener {

    void generatingStarted();
    void taskIsProcessing(int generatorNumber, long timeFromStart);
    void taskDone(GeneratingResult result);
    void generatingFinished();
    
}
