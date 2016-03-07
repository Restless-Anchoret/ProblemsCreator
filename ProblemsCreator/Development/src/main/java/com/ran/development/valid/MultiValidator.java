package com.ran.development.valid;

import com.ran.development.gen.Generator;
import com.ran.development.logging.DevelopmentLogging;
import com.ran.development.util.DevelopmentListener;
import com.ran.development.util.DevelopmentListenerSupport;
import com.ran.development.util.DevelopmentResult;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.function.Supplier;
import java.util.logging.Level;

public class MultiValidator {

    private static final long MESSAGING_DELAY = 2000;
    
    private DevelopmentListenerSupport listenerSupport = new DevelopmentListenerSupport();
    private Supplier<? extends Validator> validatorSupplier = () -> { return Validator.getDefault(); };
    private Path[] paths = { };
    private String[] arguments = { };
    
    public Supplier<? extends Validator> getValidatorSupplier() {
        return validatorSupplier;
    }

    public void setGeneratorSupplier(Supplier<? extends Validator> validatorSupplier) {
        this.validatorSupplier = validatorSupplier;
    }

    public Path[] getPaths() {
        return paths;
    }

    public void setPaths(Path[] paths) {
        this.paths = paths;
    }
    
    public String[] getArguments() {
        return arguments;
    }

    public void setArguments(String[] arguments) {
        this.arguments = arguments;
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
    
    public void performValidating() {
        listenerSupport.fireProcessingStarted();
        Validator validator = validatorSupplier.get();
        if (validator == null) {
            finishEarlier("Cannot instantiate Validator subclass", DevelopmentResult.Info.FAIL, 0, paths.length);
            return;
        }
        for (int index = 1; index <= paths.length; index++) {
            Path path = paths[index - 1];
            long start = System.currentTimeMillis();
            Thread thread = null;
            try (InputStream inputStream = new FileInputStream(path.toFile())) {
                validator.setInputStream(inputStream);
                FutureTask<DevelopmentResult> futureTask = new FutureTask<>(new ValidatingTask(validator, index, arguments));
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
            } catch (IOException exception) {
                String message = "Cannot open input file";
                DevelopmentLogging.logger.log(Level.FINE, message, exception);
                listenerSupport.fireTaskIsDone(new DevelopmentResult(index, message, DevelopmentResult.Info.FAIL,
                        System.currentTimeMillis() - start));
            } catch (InterruptedException exception) {
                thread.stop();
                listenerSupport.fireTaskIsDone(new DevelopmentResult(index, "Validating interrupted",
                        DevelopmentResult.Info.INTERRUPTED, System.currentTimeMillis() - start));
                finishEarlier("Did not run", DevelopmentResult.Info.DID_NOT_RUN, index + 1, paths.length);
                return;
            } catch (ExecutionException exception) {
                String message = "Exception while execution of validating";
                DevelopmentLogging.logger.log(Level.FINE, message, exception);
                listenerSupport.fireTaskIsDone(new DevelopmentResult(index, message, DevelopmentResult.Info.FAIL));
            }
        }
        listenerSupport.fireProcessingFinished();
    }
    
    private void finishEarlier(String message, DevelopmentResult.Info info, int from, int to) {
        for (int i = from; i < to; i++) {
            listenerSupport.fireTaskIsDone(new DevelopmentResult(i, message, info));
        }
        listenerSupport.fireProcessingFinished();
    }
    
    private static class ValidatingTask implements Callable<DevelopmentResult> {
        private String[] arguments;
        private Validator validator;
        private int validatorNumber;
        
        public ValidatingTask(Validator validator, int validatorNumber, String[] arguments) {
            this.validator = validator;
            this.validatorNumber = validatorNumber;
            this.arguments = arguments;
        }
        
        @Override
        public DevelopmentResult call() {
            long start = System.currentTimeMillis();
            try {
                validator.performValidation(arguments);
            } catch (ValidationException exception) {
                StrictInput.State state = exception.getStrictInputState();
                String message = exception.getMessage();
                if (!state.isReaden()) {
                    message += " (line = " + state.getLineNumber() + ", column = " + state.getPosition() + ")";
                }
                return new DevelopmentResult(validatorNumber, message, DevelopmentResult.Info.FAIL,
                        System.currentTimeMillis() - start);
            } catch (Throwable throwable) {
                String message = "Unexpected exception while validating";
                DevelopmentLogging.logger.log(Level.FINE, message, throwable);
                return new DevelopmentResult(validatorNumber, message, DevelopmentResult.Info.FAIL,
                        System.currentTimeMillis() - start);
            }
            return new DevelopmentResult(validatorNumber, System.currentTimeMillis() - start);
        }
    }
    
}