package com.ran.development.gen;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GeneratorListenerSupport {

    private Set<GeneratorListener> listeners = new HashSet<>();
    
    public void addGeneratorListener(GeneratorListener listener) {
        listeners.add(listener);
    }
    
    public void removeGeneratorListener(GeneratorListener listener) {
        listeners.remove(listener);
    }
    
    public Set<GeneratorListener> getGeneratorListeners() {
        return Collections.unmodifiableSet(listeners);
    }
    
    public void fireGeneratingStarted() {
        listeners.forEach(listener -> {
            listener.generatingStarted();
        });
    }
    
    public void fireTaskIsProcessing(int generatorNumber, long timeFromStart) {
        listeners.forEach(listener -> {
            listener.taskIsProcessing(generatorNumber, timeFromStart);
        });
    }
    
    public void fireTaskDone(GeneratingResult result) {
        listeners.forEach(listener -> {
            listener.taskDone(result);
        });
    }
    
    public void fireGeneratingFinished() {
        listeners.forEach(listener -> {
            listener.generatingFinished();
        });
    }
    
}