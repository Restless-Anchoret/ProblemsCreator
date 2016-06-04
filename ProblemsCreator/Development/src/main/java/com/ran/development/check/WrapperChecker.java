package com.ran.development.check;

import com.ran.development.logging.DevelopmentLogging;
import com.ran.development.util.DevelopmentListener;
import com.ran.development.util.DevelopmentListenerSupport;
import com.ran.development.util.DevelopmentResult;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.function.Supplier;
import java.util.logging.Level;

public class WrapperChecker {

    private static final long DEFAULT_DELAY_TIME = 10_000;
    private static final long MESSAGING_DELAY = 2000;

    private Supplier<? extends Checker> checkerSupplier;
    private Checker checker = null;
    private DevelopmentListenerSupport listenerSupport = new DevelopmentListenerSupport();
    private long delayTime;

    public WrapperChecker(Supplier<? extends Checker> checkerSupplier, long delayTime) {
        this.checkerSupplier = checkerSupplier;
        this.delayTime = delayTime;
    }

    public WrapperChecker(Supplier<? extends Checker> checkerSupplier) {
        this(checkerSupplier, DEFAULT_DELAY_TIME);
    }

    public void addDevelopmentListener(DevelopmentListener listener) {
        listenerSupport.addDevelopmentListener(listener);
    }

    public void removeDevelopmentListener(DevelopmentListener listener) {
        listenerSupport.removeDevelopmentListener(listener);
    }

    public Set<DevelopmentListener> getDevelopmentListeners() {
        return listenerSupport.getDevelopmentListeners();
    }

    public int check(Path inputPath, Path outputPath, Path answerPath) {
        listenerSupport.fireTaskProcessingStarted(0);
        if (checker == null) {
            checker = checkerSupplier.get();
        }
        if (checker == null) {
            String message = "Cannot load Checker subclass";
            DevelopmentLogging.logger.fine(message);
            listenerSupport.fireTaskIsDone(new DevelopmentResult(0, message, Checker.FAIL));
            return Checker.FAIL;
        }
        long start = System.currentTimeMillis();
        Thread thread = null;
        try (InputStream input = Files.newInputStream(inputPath);
                InputStream output = Files.newInputStream(outputPath);
                InputStream answer = Files.newInputStream(answerPath)) {
            checker.setInput(input);
            checker.setOutput(output);
            checker.setAnswer(answer);
            FutureTask<CheckingResult> futureTask = new FutureTask<>(new CheckingTask(checker));
            thread = new Thread(futureTask);
            start = System.currentTimeMillis();
            thread.start();
            while (thread.isAlive()) {
                thread.join(MESSAGING_DELAY);
                if (thread.isAlive()) {
                    long time = System.currentTimeMillis() - start;
                    if (delayTime > 0 && time > delayTime) {
                        thread.stop();
                        String message = "Checker is working longer than expected";
                        DevelopmentLogging.logger.fine(message);
                        listenerSupport.fireTaskIsDone(new DevelopmentResult(0, message,
                                DevelopmentResult.FAIL, time));
                        return Checker.FAIL;
                    }
                    listenerSupport.fireTaskIsProcessing(0, time);
                }
            }
            CheckingResult checkingResult = futureTask.get();
            if (checkingResult.getCheckingInfo() == Checker.FAIL) {
                DevelopmentLogging.logger.fine(checkingResult.getDevelopmentResult()
                        .getMessage());
            }
            listenerSupport.fireTaskIsDone(checkingResult.getDevelopmentResult());
            return checkingResult.getCheckingInfo();
        } catch (IOException exception) {
            String message = "Cannot open one of input files";
            DevelopmentLogging.logger.log(Level.FINE, message, exception);
            listenerSupport.fireTaskIsDone(new DevelopmentResult(0, message,
                    DevelopmentResult.FAIL, System.currentTimeMillis() - start));
        } catch (InterruptedException exception) {
            thread.stop();
            listenerSupport.fireTaskIsDone(new DevelopmentResult(0, "Checking interrupted",
                    DevelopmentResult.INTERRUPTED, System.currentTimeMillis() - start));
        } catch (ExecutionException exception) {
            String message = "Exception while execution of checking";
            DevelopmentLogging.logger.log(Level.FINE, message, exception);
            listenerSupport.fireTaskIsDone(new DevelopmentResult(0, message,
                    DevelopmentResult.FAIL));
        }
        return Checker.FAIL;
    }

    private static class CheckingResult {
        private DevelopmentResult developmentResult;
        private int checkingInfo;

        public CheckingResult(DevelopmentResult developmentResult, int checkingInfo) {
            this.developmentResult = developmentResult;
            this.checkingInfo = checkingInfo;
        }

        public DevelopmentResult getDevelopmentResult() {
            return developmentResult;
        }

        public int getCheckingInfo() {
            return checkingInfo;
        }
    }

    private static class CheckingTask implements Callable<CheckingResult> {
        private Checker checker;

        public CheckingTask(Checker checker) {
            this.checker = checker;
        }

        @Override
        public CheckingResult call() {
            long start = System.currentTimeMillis();
            try {
                checker.performChecking();
            } catch (CheckResultException exception) {
                int resultInfo = exception.getResultInfo();
                int developmentResultInfo = (resultInfo == Checker.FAIL
                        ? DevelopmentResult.FAIL : DevelopmentResult.OK);
                DevelopmentResult developmentResult = new DevelopmentResult(
                        0, exception.getMessage(), developmentResultInfo,
                        System.currentTimeMillis() - start);
                return new CheckingResult(developmentResult, resultInfo);
            } catch (Throwable throwable) {
                String message = "Unexpected exception while checking";
                DevelopmentLogging.logger.log(Level.FINE, message, throwable);
                DevelopmentResult developmentResult = new DevelopmentResult(0, message,
                        DevelopmentResult.FAIL, System.currentTimeMillis() - start);
                return new CheckingResult(developmentResult, Checker.FAIL);
            }
            String message = "Checker did not throw any expected exception while its work";
            DevelopmentLogging.logger.fine(message);
            DevelopmentResult developmentResult = new DevelopmentResult(0, message,
                    DevelopmentResult.FAIL, System.currentTimeMillis() - start);
            return new CheckingResult(developmentResult, Checker.FAIL);
        }
    }

}
