package com.ran.development.gen;

import com.ran.development.util.DevelopmentResult;
import com.ran.development.util.DevelopmentListenerSupport;
import com.ran.development.logging.DevelopmentLogging;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.function.Supplier;
import java.util.logging.Level;
import com.ran.development.util.DevelopmentListener;
import java.nio.file.Files;

public class MultiGenerator {
    
    private static final long MESSAGING_DELAY = 2000;
    
    private DevelopmentListenerSupport listenerSupport = new DevelopmentListenerSupport();
    private Supplier<? extends Generator> generatorSupplier = () -> {
        return Generator.getDefault();
    };
    private int randomSeed = 0;
    private Path[] paths = { };
    private String[] arguments = { };
    
    public Supplier<? extends Generator> getGeneratorSupplier() {
        return generatorSupplier;
    }

    public void setGeneratorSupplier(Supplier<? extends Generator> generatorSupplier) {
        this.generatorSupplier = generatorSupplier;
    }

    public int getRandomSeed() {
        return randomSeed;
    }

    public void setRandomSeed(int randomSeed) {
        this.randomSeed = randomSeed;
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
    
    public void performGenerating() {
        listenerSupport.fireProcessingStarted();
        Generator generator = generatorSupplier.get();
        if (generator == null) {
            finishEarlier("Cannot instantiate Generator subclass",
                    DevelopmentResult.FAIL, 1, paths.length);
            return;
        }
        generator.setRandomSeed(randomSeed);
        for (int index = 1; index <= paths.length; index++) {
            listenerSupport.fireTaskProcessingStarted(index);
            Path path = paths[index - 1];
            long start = System.currentTimeMillis();
            Thread thread = null;
            try (OutputStream output = Files.newOutputStream(path)) {
                generator.setOutput(output);
                FutureTask<DevelopmentResult> futureTask = new FutureTask<>(
                        new GeneratingTask(generator, index, arguments));
                thread = new Thread(futureTask);
                start = System.currentTimeMillis();
                thread.start();
                while (thread.isAlive()) {
                    thread.join(MESSAGING_DELAY);
                    if (thread.isAlive()) {
                        listenerSupport.fireTaskIsProcessing(index,
                                System.currentTimeMillis() - start);
                    }
                }
                listenerSupport.fireTaskIsDone(futureTask.get());
            } catch (IOException exception) {
                String message = "Cannot open output file";
                DevelopmentLogging.logger.log(Level.FINE, message, exception);
                listenerSupport.fireTaskIsDone(new DevelopmentResult(index, message,
                        DevelopmentResult.FAIL, System.currentTimeMillis() - start));
            } catch (InterruptedException exception) {
                thread.stop();
                listenerSupport.fireTaskIsDone(new DevelopmentResult(index,
                        "Generating interrupted", DevelopmentResult.INTERRUPTED,
                        System.currentTimeMillis() - start));
                finishEarlier("Did not run", DevelopmentResult.DID_NOT_RUN,
                        index + 1, paths.length);
                return;
            } catch (ExecutionException exception) {
                String message = "Exception while execution of generating";
                DevelopmentLogging.logger.log(Level.FINE, message, exception);
                listenerSupport.fireTaskIsDone(new DevelopmentResult(
                        index, message, DevelopmentResult.FAIL));
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
    
    private static class GeneratingTask implements Callable<DevelopmentResult> {
        private String[] arguments;
        private Generator generator;
        private int generatorNumber;
        
        public GeneratingTask(Generator generator, int generatorNumber, String[] arguments) {
            this.generator = generator;
            this.generatorNumber = generatorNumber;
            this.arguments = arguments;
        }
        
        @Override
        public DevelopmentResult call() {
            long start = System.currentTimeMillis();
            try {
                generator.generate(arguments);
            } catch (Throwable throwable) {
                String message = "Error or exception while generating";
                DevelopmentLogging.logger.log(Level.FINE, message, throwable);
                return new DevelopmentResult(generatorNumber, message, DevelopmentResult.FAIL,
                        System.currentTimeMillis() - start);
            }
            return new DevelopmentResult(generatorNumber, System.currentTimeMillis() - start);
        }
    }
    
}