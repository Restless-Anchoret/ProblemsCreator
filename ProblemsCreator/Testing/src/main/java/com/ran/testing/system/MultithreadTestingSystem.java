package com.ran.testing.system;

import com.ran.testing.logging.TestingLogging;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;

public class MultithreadTestingSystem implements TestingSystem {

    private static final int DEFAULT_THREADS_QUANTITY = Runtime.getRuntime()
            .availableProcessors();
    private static MultithreadTestingSystem defaultSystem = null;
    
    public static MultithreadTestingSystem getDefault() {
        if (defaultSystem == null) {
            defaultSystem = new MultithreadTestingSystem(DEFAULT_THREADS_QUANTITY);
        }
        return defaultSystem;
    }
    
    private int threadsQuantity;
    private TestingFileSupplier fileSupplier;
    private ExecutorService service;
    private ExecutorCompletionService<TestingInfo> completionService;
    private int submissionsCounter = 0;
    private Thread resultHandlersThread;

    public MultithreadTestingSystem(int threadsQuantity, TestingFileSupplier fileSupplier) {
        this.threadsQuantity = threadsQuantity;
        this.fileSupplier = fileSupplier;
    }

    public MultithreadTestingSystem(int threadsQuantity) {
        this(threadsQuantity, null);
    }

    public MultithreadTestingSystem() {
        this(DEFAULT_THREADS_QUANTITY, null);
    }

    public int getThreadsQuantity() {
        return threadsQuantity;
    }

    public void setThreadsQuantity(int threadsQuantity) {
        if (service == null) {
            this.threadsQuantity = threadsQuantity;
        }
    }

    public TestingFileSupplier getFileSupplier() {
        return fileSupplier;
    }

    public void setFileSupplier(TestingFileSupplier fileSupplier) {
        this.fileSupplier = fileSupplier;
    }

    public synchronized int getSubmissionsCounter() {
        return submissionsCounter;
    }
    
    private synchronized void submissionsCounterUp() {
        submissionsCounter++;
    }
    
    private synchronized void submissionsCounterDown() {
        submissionsCounter--;
    }
    
    @Override
    public void start() {
        service = Executors.newFixedThreadPool(threadsQuantity);
        completionService = new ExecutorCompletionService<>(service);
        resultHandlersThread = new Thread(new ResultTracking());
        resultHandlersThread.start();
    }

    @Override
    public void addSubmission(TestingInfo info) {
        submissionsCounterUp();
        completionService.submit(new TestingTask(info, fileSupplier));
    }

    @Override
    public void stop() {
        service.shutdown();
        resultHandlersThread.interrupt();
    }
    
    private class ResultTracking implements Runnable {
        public void run() {
            boolean interrupted = false;
            while (!interrupted || getSubmissionsCounter() > 0) {
                try {
                    Future<TestingInfo> future = completionService.take();
                    submissionsCounterDown();
                    TestingInfo info = future.get();
                    info.getTestResultHandler().process(info);
                } catch (InterruptedException exception) {
                    if (interrupted) {
                        TestingLogging.logger.log(Level.FINE,
                                "Result tracking process was interrupted more than one time", exception);
                    }
                    interrupted = true;
                } catch (ExecutionException exception) {
                    TestingLogging.logger.log(Level.FINE,
                            "Unhandled exception while testing submission in its own thread", exception);
                    exception.printStackTrace();
                }
            }
        }
    }
    
    private static class TestingTask implements Callable<TestingInfo> {
        private TestingInfo info;
        private TestingFileSupplier fileSupplier;
        public TestingTask(TestingInfo info, TestingFileSupplier fileSupplier) {
            this.info = info;
            this.fileSupplier = fileSupplier;
        }
        public TestingInfo call() {
            info.getProblemTester().performTesting(fileSupplier, info);
            return info;
        }
    }
    
}