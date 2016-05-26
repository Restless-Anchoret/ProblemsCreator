package com.ran.interaction.strategy;

import com.ran.development.gen.Generator;
import com.ran.development.gen.MultiGenerator;
import com.ran.development.util.DevelopmentAdapter;
import com.ran.development.util.DevelopmentListener;
import com.ran.development.util.DevelopmentResult;
import com.ran.development.util.Utils;
import com.ran.interaction.panels.GeneratorsPanel;
import com.ran.interaction.support.SwingUtil;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class GeneratingStrategy extends AbstractDevelopmentStrategy {

    private MultiGenerator multiGenerator;
    private String problemFolder;
    private String testGroupToGenerate;
    private boolean generatingSuccess = true;
    private boolean generatingDone = false;
    private boolean testsHaveBeenSaved = false;
    
    public GeneratingStrategy(MultiGenerator multiGenerator) {
        this.multiGenerator = multiGenerator;
        initializeSpecialListener();
    }
    
    private void initializeSpecialListener() {
        this.multiGenerator.addDevelopmentListener(new DevelopmentAdapter() {
            @Override
            public void taskIsDone(DevelopmentResult result) {
                if (result.getInfo() != DevelopmentResult.OK) {
                    generatingSuccess = false;
                }
            }
            @Override
            public void processingFinished() {
                generatingDone = true;
            }
        });
    }

    public void setProblemFolder(String problemFolder) {
        this.problemFolder = problemFolder;
    }

    public void setTestGroupToGenerate(String testGroupToGenerate) {
        this.testGroupToGenerate = testGroupToGenerate;
    }

    @Override
    public boolean isNeedSaveAbility() {
        return true;
    }
    
    @Override
    public void setOutputConsumer(Consumer<String> outputConsumer) {
        super.setOutputConsumer(outputConsumer);
        for (DevelopmentListener listener: multiGenerator.getDevelopmentListeners()) {
            if (listener instanceof GeneratingListener) {
                multiGenerator.removeDevelopmentListener(listener);
            }
        }
        multiGenerator.addDevelopmentListener(new GeneratingListener(outputConsumer));
    }

    @Override
    protected void performDevelopmentProcess() {
        multiGenerator.setGeneratorSupplier(Utils.getSupplier(Generator.class,
                getCodeSupplier().getCompileFile()));
        multiGenerator.performGenerating();
    }

    @Override
    public void saveDevelopmentResults() {
        if (testsHaveBeenSaved) {
            getOutputConsumer().accept("Tests have already been saved.");
            return;
        }
        if (!generatingDone || !generatingSuccess) {
            SwingUtil.showErrorDialog(null, GeneratorsPanel.CANNOT_SAVE_MESSAGE,
                    GeneratorsPanel.CANNOT_SAVE_TITLE);
            return;
        }
        Path[] generatedTests = multiGenerator.getPaths();
        List<Path> generatedTestsList = Arrays.asList(generatedTests);
        boolean addingResult = getFileSupplier().addTestInputFiles(
                problemFolder, testGroupToGenerate, generatedTestsList);
        if (addingResult) {
            testsHaveBeenSaved = true;
            getOutputConsumer().accept("Tests were saved successfully.");
        } else {
            getOutputConsumer().accept("Error while saving tests.");
        }
    }
    
    private static class GeneratingListener implements DevelopmentListener {
        
        private Consumer<String> outputConsumer;

        public GeneratingListener(Consumer<String> outputConsumer) {
            this.outputConsumer = outputConsumer;
        }
        
        @Override
        public void processingStarted() {
            outputConsumer.accept("Generating has started.\n");
        }

        @Override
        public void taskProcessingStarted(int taskNumber) {
            outputConsumer.accept("Test #" + taskNumber + " generating started.");
        }

        @Override
        public void taskIsProcessing(int taskNumber, long timeFromStart) {
            outputConsumer.accept("Test #" + taskNumber + " is generating ..... " + timeFromStart + " ms.");
        }

        @Override
        public void taskIsDone(DevelopmentResult result) {
            outputConsumer.accept("Test #" + result.getTaskNumber() + " generating is done in " + result.getTime() + " milliseconds.");
            outputConsumer.accept("Message: " + result.getMessage() + ".\n");
        }

        @Override
        public void processingFinished() {
            outputConsumer.accept("Generating process is done");
        }
        
    }

}