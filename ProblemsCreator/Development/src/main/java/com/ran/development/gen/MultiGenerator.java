package com.ran.development.gen;

import com.ran.development.logging.DevelopmentLogging;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.function.Supplier;
import java.util.logging.Level;

public class MultiGenerator {
    
    private static final long MESSAGING_DELAY = 2000;
    
    private GeneratorListenerSupport listenerSupport = new GeneratorListenerSupport();
    private Supplier<? extends Generator> generatorSupplier = () -> { return Generator.getDefault(); };
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
    
    public void addGeneratorListener(GeneratorListener listener) {
        listenerSupport.addGeneratorListener(listener);
    }
    
    public void removeGeneratorListener(GeneratorListener listener) {
        listenerSupport.removeGeneratorListener(listener);
    }
    
    public Set<GeneratorListener> getGeneratorListeners() {
        return listenerSupport.getGeneratorListeners();
    }
    
    public void performGenerating() {
        listenerSupport.fireGeneratingStarted();
        Generator generator = generatorSupplier.get();
        if (generator == null) {
            finishEarlier("Cannot instantiate Generator subclass", GeneratingResult.Info.FAIL, 0, paths.length);
            return;
        }
        generator.setRandomSeed(randomSeed);
        for (int index = 1; index <= paths.length; index++) {
            Path path = paths[index - 1];
            long start = System.currentTimeMillis();
            Thread thread = null;
            try (OutputStream output = new FileOutputStream(path.toFile())) {
                generator.setOutput(output);
                FutureTask<GeneratingResult> futureTask = new FutureTask<>(new GeneratingTask(generator, index, arguments));
                thread = new Thread(futureTask);
                start = System.currentTimeMillis();
                thread.start();
                while (thread.isAlive()) {
                    thread.join(MESSAGING_DELAY);
                    if (thread.isAlive()) {
                        listenerSupport.fireTaskIsProcessing(index, System.currentTimeMillis() - start);
                    }
                }
                listenerSupport.fireTaskDone(futureTask.get());
            } catch (IOException exception) {
                String message = "Cannot open output file";
                DevelopmentLogging.logger.log(Level.FINE, message, exception);
                listenerSupport.fireTaskDone(new GeneratingResult(index, message, GeneratingResult.Info.FAIL,
                        System.currentTimeMillis() - start));
            } catch (InterruptedException exception) {
                thread.stop();
                listenerSupport.fireTaskDone(new GeneratingResult(index, "Generating interrupted",
                        GeneratingResult.Info.INTERRUPTED, System.currentTimeMillis() - start));
                finishEarlier("Did not run", GeneratingResult.Info.DID_NOT_RUN, index + 1, paths.length);
                return;
            } catch (ExecutionException exception) {
                String message = "Exception while execution of generating";
                DevelopmentLogging.logger.log(Level.FINE, message, exception);
                listenerSupport.fireTaskDone(new GeneratingResult(index, message, GeneratingResult.Info.FAIL));
            }
        }
        listenerSupport.fireGeneratingFinished();
    }
    
    private void finishEarlier(String message, GeneratingResult.Info info, int from, int to) {
        for (int i = from; i < to; i++) {
            listenerSupport.fireTaskDone(new GeneratingResult(i, message, info));
        }
        listenerSupport.fireGeneratingFinished();
    }
    
    private static class GeneratingTask implements Callable<GeneratingResult> {
        private String[] arguments;
        private Generator generator;
        private int generatorNumber;
        
        public GeneratingTask(Generator generator, int generatorNumber, String[] arguments) {
            this.generator = generator;
            this.generatorNumber = generatorNumber;
            this.arguments = arguments;
        }
        
        @Override
        public GeneratingResult call() {
            long start = System.currentTimeMillis();
            try {
                generator.generate(arguments);
            } catch (Throwable throwable) {
                String message = "Error or exception while generating";
                DevelopmentLogging.logger.log(Level.FINE, message, throwable);
                return new GeneratingResult(generatorNumber, message, GeneratingResult.Info.FAIL,
                        System.currentTimeMillis() - start);
            }
            return new GeneratingResult(generatorNumber, System.currentTimeMillis() - start);
        }
    }
    
}