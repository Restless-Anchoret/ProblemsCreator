package com.ran.interaction.strategy;

import com.ran.development.util.DevelopmentListener;
import com.ran.development.util.DevelopmentResult;
import com.ran.development.util.Utils;
import com.ran.development.valid.MultiValidator;
import com.ran.development.valid.Validator;
import java.util.function.Consumer;

public class ValidatingStrategy extends AbstractDevelopmentStrategy {

    private MultiValidator multiValidator;

    public ValidatingStrategy(MultiValidator multiValidator) {
        this.multiValidator = multiValidator;
    }
    
    @Override
    public void setOutputConsumer(Consumer<String> outputConsumer) {
        super.setOutputConsumer(outputConsumer);
        for (DevelopmentListener listener: multiValidator.getDevelopmentListeners()) {
            multiValidator.removeDevelopmentListener(listener);
        }
        multiValidator.addDevelopmentListener(new ValidatingListener(outputConsumer));
    }

    @Override
    protected void performDevelopmentProcess() {
        multiValidator.setValidatorSupplier(Utils.getSupplier(Validator.class,
                getCodeSupplier().getCompileFile()));
        multiValidator.performValidating();
    }
    
    private static class ValidatingListener implements DevelopmentListener {
        
        private Consumer<String> outputConsumer;

        public ValidatingListener(Consumer<String> outputConsumer) {
            this.outputConsumer = outputConsumer;
        }
        
        @Override
        public void processingStarted() {
            outputConsumer.accept("Validating has started.\n");
        }

        @Override
        public void taskProcessingStarted(int taskNumber) {
            outputConsumer.accept("Test #" + taskNumber + " validating started.");
        }

        @Override
        public void taskIsProcessing(int taskNumber, long timeFromStart) {
            outputConsumer.accept("Test #" + taskNumber + " is validating ..... " + timeFromStart + " ms.");
        }

        @Override
        public void taskIsDone(DevelopmentResult result) {
            outputConsumer.accept("Test #" + result.getTaskNumber() + " validating is done in " + result.getTime() + " milliseconds.");
            outputConsumer.accept("Message: " + result.getMessage() + ".\n");
        }

        @Override
        public void processingFinished() {
            outputConsumer.accept("Validating process is done");
        }
        
    }
    
}