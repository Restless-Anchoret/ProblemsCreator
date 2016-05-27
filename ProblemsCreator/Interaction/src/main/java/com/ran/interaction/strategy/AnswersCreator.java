package com.ran.interaction.strategy;

import com.ran.development.logging.DevelopmentLogging;
import com.ran.development.util.DevelopmentListener;
import com.ran.development.util.DevelopmentListenerSupport;
import com.ran.development.util.DevelopmentResult;
import com.ran.testing.language.LanguageToolkit;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;

public class AnswersCreator {
    
    private static final long MESSAGING_DELAY = 2000;
    
    private DevelopmentListenerSupport listenerSupport = new DevelopmentListenerSupport();
    private Path authorDecisionClassPath = null;
    private List<Path> pathInputs = null;
    private List<Path> pathAnswers = null;
    private LanguageToolkit languageToolkit = null;
    private Path configurationFolder = null;
    private int timeLimit;
    private int memoryLimit;

    public Path getAuthorDecisionClassPath() {
        return authorDecisionClassPath;
    }

    public void setAuthorDecisionClassPath(Path authorDecisionClassPath) {
        this.authorDecisionClassPath = authorDecisionClassPath;
    }

    public void setPathInputs(List<Path> pathInputs) {
        this.pathInputs = pathInputs;
    }

    public List<Path> getPathAnswers() {
        return pathAnswers;
    }

    public void setPathAnswers(List<Path> pathAnswers) {
        this.pathAnswers = pathAnswers;
    }

    public LanguageToolkit getLanguageToolkit() {
        return languageToolkit;
    }

    public void setLanguageToolkit(LanguageToolkit languageToolkit) {
        this.languageToolkit = languageToolkit;
    }

    public void setConfigurationFolder(Path configurationFolder) {
        this.configurationFolder = configurationFolder;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public void setMemoryLimit(int memoryLimit) {
        this.memoryLimit = memoryLimit;
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
    
    public void performCreatingAnswers() {
        listenerSupport.fireProcessingStarted();
        for (int index = 1; index <= pathInputs.size(); index++) {
            listenerSupport.fireTaskProcessingStarted(index);
            Path pathInput = pathInputs.get(index - 1);
            Path pathOutput = pathAnswers.get(index - 1);
            long start = System.currentTimeMillis();
            Thread thread = null;
            try {
                FutureTask<DevelopmentResult> futureTask = new FutureTask<>(
                        new CreatingAnswerTask(languageToolkit, authorDecisionClassPath,
                                pathInput, pathOutput, configurationFolder,
                                timeLimit, (short)memoryLimit, index));
                thread = new Thread(futureTask);
                start = System.currentTimeMillis();
                thread.start();
                while (thread.isAlive()) {
                    thread.join(MESSAGING_DELAY);
                    if (thread.isAlive()) {
                        listenerSupport.fireTaskIsProcessing(index, System.currentTimeMillis() - start);
                    }
                }
                listenerSupport.fireTaskIsDone(futureTask.get());
            } catch (InterruptedException exception) {
                thread.interrupt();
                listenerSupport.fireTaskIsDone(new DevelopmentResult(index, "Creating answer interrupted",
                        DevelopmentResult.INTERRUPTED, System.currentTimeMillis() - start));
                finishEarlier("Did not run", DevelopmentResult.DID_NOT_RUN, index + 1, pathInputs.size());
                return;
            } catch (ExecutionException exception) {
                String message = "Exception while execution of creating answer";
                DevelopmentLogging.logger.log(Level.FINE, message, exception);
                listenerSupport.fireTaskIsDone(new DevelopmentResult(index, message, DevelopmentResult.FAIL));
            }
        }
        listenerSupport.fireProcessingFinished();
    }
    
    private void finishEarlier(String message, int info, int from, int to) {
        for (int i = from; i <= to; i++) {
            listenerSupport.fireTaskIsDone(new DevelopmentResult(i, message, info));
        }
        listenerSupport.fireProcessingFinished();
    }
    
    private static class CreatingAnswerTask implements Callable<DevelopmentResult> {
        private LanguageToolkit languageToolkit;
        private Path compileFile;
        private Path inputFile;
        private Path outputFile;
        private Path configFolder;
        private int timeLimit;
        private short memoryLimit;
        private int taskNumber;

        public CreatingAnswerTask(LanguageToolkit languageToolkit,
                Path compileFile,Path inputFile, Path outputFile, Path configFolder,
                int timeLimit, short memoryLimit, int taskNumber) {
            this.languageToolkit = languageToolkit;
            this.compileFile = compileFile;
            this.inputFile = inputFile;
            this.outputFile = outputFile;
            this.configFolder = configFolder;
            this.timeLimit = timeLimit;
            this.memoryLimit = memoryLimit;
            this.taskNumber = taskNumber;
        }
        
        @Override
        public DevelopmentResult call() {
            long start = System.currentTimeMillis();
            try {
                LanguageToolkit.ExecutionInfo info = languageToolkit.execute(compileFile,
                        inputFile, outputFile, configFolder, timeLimit, memoryLimit);
                long time = (info.getDecisionTime() == null ? System.currentTimeMillis() - start :
                        info.getDecisionTime().longValue());
                return new DevelopmentResult(taskNumber, time);
            } catch (Throwable throwable) {
                String message = "Error or exception while creating answer: " + throwable.getClass().getName();
                DevelopmentLogging.logger.log(Level.FINE, message, throwable);
                return new DevelopmentResult(taskNumber, message, DevelopmentResult.FAIL,
                        System.currentTimeMillis() - start);
            }
        }
    }
    
}