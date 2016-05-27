package com.ran.interaction.strategy;

import com.ran.development.util.DevelopmentListener;
import com.ran.development.util.DevelopmentResult;
import com.ran.filesystem.descriptor.ProblemDescriptor;
import com.ran.filesystem.supplier.FileSupplier;
import com.ran.interaction.panels.AuthorDecisionsPanel;
import com.ran.interaction.support.SwingUtil;
import com.ran.testing.language.LanguageToolkit;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

public class CreatingAnswersStrategy extends AbstractDevelopmentStrategy {

    private AnswersCreator answersCreator;
    private String problemFolder;
    private List<String> testGroupTypes;

    public CreatingAnswersStrategy(AnswersCreator answersCreator) {
        this.answersCreator = answersCreator;
    }

    public void setProblemFolder(String problemFolder) {
        this.problemFolder = problemFolder;
    }

    public void setTestGroupTypes(List<String> testGroupTypes) {
        this.testGroupTypes = testGroupTypes;
    }

    @Override
    public void setLanguageToolkit(LanguageToolkit languageToolkit) {
        super.setLanguageToolkit(languageToolkit);
        answersCreator.setLanguageToolkit(languageToolkit);
    }
    
    @Override
    public boolean isNeedSaveAbility() {
        return true;
    }
    
    @Override
    public void setOutputConsumer(Consumer<String> outputConsumer) {
        super.setOutputConsumer(outputConsumer);
        for (DevelopmentListener listener: answersCreator.getDevelopmentListeners()) {
            if (listener instanceof AnswersCreatingListener) {
                answersCreator.removeDevelopmentListener(listener);
            }
        }
        answersCreator.addDevelopmentListener(new AnswersCreatingListener(outputConsumer));
    }

    @Override
    protected void addSuccessDevelopmentListener(DevelopmentListener listener) {
        answersCreator.addDevelopmentListener(listener);
    }
    
    @Override
    protected void performDevelopmentProcess() {
        ProblemDescriptor descriptor = getFileSupplier().getProblemDescriptor(problemFolder);
        answersCreator.setAuthorDecisionClassPath(getCodeSupplier().getCompileFile());
        answersCreator.setTimeLimit(descriptor.getTimeLimit());
        answersCreator.setMemoryLimit(descriptor.getMemoryLimit());
        answersCreator.setConfigurationFolder(getFileSupplier().getConfigurationFolder());
        answersCreator.performCreatingAnswers();
    }

    @Override
    public void saveDevelopmentResults() {
        if (isResultsHaveBeenSaved()) {
            getOutputConsumer().accept("Answers have already been saved.");
            return;
        }
        if (!isDevelopmentDone() || !isDevelopmentSuccess()) {
            System.out.println(isDevelopmentDone());
            System.out.println(isDevelopmentSuccess());
            SwingUtil.showErrorDialog(null, AuthorDecisionsPanel.CANNOT_SAVE_MESSAGE,
                    AuthorDecisionsPanel.CANNOT_SAVE_TITLE);
            return;
        }
        List<Path> pathAnswers = answersCreator.getPathAnswers();
        FileSupplier fileSupplier = getFileSupplier();
        int countedTests = 0;
        for (String testGroupType: testGroupTypes) {
            int testsInGroup = fileSupplier.getTestsQuantity(problemFolder, testGroupType);
            for (int testNumber = 1; testNumber <= testsInGroup; testNumber++) {
                Path answerPath = pathAnswers.get(countedTests + testNumber - 1);
                fileSupplier.addTestAnswerFile(problemFolder, testGroupType, testNumber, answerPath);
            }
            countedTests += testsInGroup;
        }
        setResultsHaveBeenSaved(true);
        getOutputConsumer().accept("Answers were saved successfully.");
    }
    
    private static class AnswersCreatingListener implements DevelopmentListener {
        
        private Consumer<String> outputConsumer;

        public AnswersCreatingListener(Consumer<String> outputConsumer) {
            this.outputConsumer = outputConsumer;
        }
        
        @Override
        public void processingStarted() {
            outputConsumer.accept("Answers creating has started.\n");
        }

        @Override
        public void taskProcessingStarted(int taskNumber) {
            outputConsumer.accept("Answer #" + taskNumber + " creating started.");
        }

        @Override
        public void taskIsProcessing(int taskNumber, long timeFromStart) {
            outputConsumer.accept("Answer #" + taskNumber + " is creating ..... " + timeFromStart + " ms.");
        }

        @Override
        public void taskIsDone(DevelopmentResult result) {
            outputConsumer.accept("Answer #" + result.getTaskNumber() + " creating is done in " + result.getTime() + " milliseconds.");
            outputConsumer.accept("Message: " + result.getMessage() + ".\n");
        }

        @Override
        public void processingFinished() {
            outputConsumer.accept("Answer creating process is done");
        }
        
    }

}