package com.ran.development.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DevelopmentListenerSupport {

    private Set<DevelopmentListener> listeners = new HashSet<>();
    
    public void addDevelopmentListener(DevelopmentListener listener) {
        listeners.add(listener);
    }
    
    public void removeDevelopmentListener(DevelopmentListener listener) {
        listeners.remove(listener);
    }
    
    public Set<DevelopmentListener> getDevelopmentListeners() {
        return Collections.unmodifiableSet(listeners);
    }
    
    public void fireProcessingStarted() {
        listeners.forEach(listener -> {
            listener.processingStarted();
        });
    }
    
    public void fireTaskProcessingStarted(int taskNumber) {
        listeners.forEach(listener -> {
            listener.taskProcessingStarted(taskNumber);
        });
    }
    
    public void fireTaskIsProcessing(int taskNumber, long timeFromStart) {
        listeners.forEach(listener -> {
            listener.taskIsProcessing(taskNumber, timeFromStart);
        });
    }
    
    public void fireTaskIsDone(DevelopmentResult result) {
        listeners.forEach(listener -> {
            listener.taskIsDone(result);
        });
    }
    
    public void fireProcessingFinished() {
        listeners.forEach(listener -> {
            listener.processingFinished();
        });
    }
    
}